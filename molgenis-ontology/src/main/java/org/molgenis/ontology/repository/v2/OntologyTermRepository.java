package org.molgenis.ontology.repository.v2;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.molgenis.data.DataService;
import org.molgenis.data.Entity;
import org.molgenis.data.EntityMetaData;
import org.molgenis.data.Repository;
import org.molgenis.data.support.MapEntity;
import org.molgenis.data.support.QueryImpl;
import org.molgenis.data.support.UuidGenerator;
import org.molgenis.ontology.model.OntologyMetaData;
import org.molgenis.ontology.model.OntologyTermDynamicAnnotationMetaData;
import org.molgenis.ontology.model.OntologyTermMetaData;
import org.molgenis.ontology.model.OntologyTermSynonymMetaData;
import org.molgenis.ontology.utils.OntologyLoader;
import org.molgenis.util.ApplicationContextProvider;
import org.semanticweb.owlapi.model.OWLClass;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class OntologyTermRepository implements Repository
{
	// private final static String PSEUDO_ROOT_CLASS_LABEL = "top";
	private final OntologyLoader ontologyLoader;
	private final DataService dataService;
	private final UuidGenerator uuidGenerator;
	private final OntologyTermDynamicAnnotationRepository ontologyTermDynamicAnnotationRepo;
	private final OntologyTermSynonymRepository ontologyTermSynonymRepo;

	public OntologyTermRepository(OntologyLoader ontologyLoader, UuidGenerator uuidGenerator,
			OntologyTermDynamicAnnotationRepository ontologyTermDynamicAnnotationRepo,
			OntologyTermSynonymRepository ontologyTermSynonymRepo)
	{
		this.ontologyLoader = ontologyLoader;
		this.uuidGenerator = uuidGenerator;
		this.dataService = ApplicationContextProvider.getApplicationContext().getBean(DataService.class);
		this.ontologyTermDynamicAnnotationRepo = ontologyTermDynamicAnnotationRepo;
		this.ontologyTermSynonymRepo = ontologyTermSynonymRepo;
	}

	@Override
	public Iterator<Entity> iterator()
	{
		// final TreeTraverser<OWLClassContainer> traverser = new TreeTraverser<OWLClassContainer>()
		// {
		// public Iterable<OWLClassContainer> children(OWLClassContainer container)
		// {
		// int count = 0;
		// List<OWLClassContainer> containers = new ArrayList<OWLClassContainer>();
		// for (OWLClass childClass : ontologyLoader.getChildClass(container.getOwlClass()))
		// {
		// containers
		// .add(new OWLClassContainer(childClass, constructNodePath(container.getNodePath(), count)));
		// count++;
		// }
		// return containers;
		// }
		// };

		return new Iterator<Entity>()
		{
			// private final OWLClass pseudoRootClass = ontologyLoader.createClass(PSEUDO_ROOT_CLASS_LABEL,
			// ontologyLoader.getRootClasses());
			// private final Iterator<OWLClassContainer> iterator = traverser.preOrderTraversal(
			// new OWLClassContainer(pseudoRootClass, "0[0]")).iterator();
			private final Iterator<OWLClass> iterator = ontologyLoader.getAllclasses().iterator();

			public boolean hasNext()
			{
				return iterator.hasNext();
			}

			public Entity next()
			{
				// OWLClassContainer container = iterator.next();
				// OWLClass cls = container.getOwlClass();
				OWLClass cls = iterator.next();
				String ontologyIRI = ontologyLoader.getOntologyIRI();
				String ontologyTermIRI = cls.getIRI().toString();
				String ontologyTermName = ontologyLoader.getLabel(cls);

				Set<String> synonymIds = FluentIterable.from(ontologyLoader.getSynonyms(cls))
						.transform(new Function<String, String>()
						{
							public String apply(String synonym)
							{
								return ontologyTermSynonymRepo.getReferenceIds().get(ontologyTermIRI).get(synonym);
							}
						}).filter(new Predicate<String>()
						{
							public boolean apply(final String synonym)
							{
								return StringUtils.isEmpty(synonym);
							}
						}).toSet();

				Set<String> annotationIds = FluentIterable.from(ontologyLoader.getDatabaseIds(cls))
						.transform(new Function<String, String>()
						{
							public String apply(String annotation)
							{
								return ontologyTermDynamicAnnotationRepo.getReferenceIds().get(ontologyTermIRI)
										.get(annotation);
							}
						}).toSet();

				Entity entity = new MapEntity();
				entity.set(OntologyTermMetaData.ID, uuidGenerator.generateId());
				entity.set(OntologyTermMetaData.ONTOLOGY_TERM_IRI, ontologyTermIRI);
				entity.set(OntologyTermMetaData.ONTOLOGY_TERM_NAME, ontologyTermName);
				entity.set(OntologyTermMetaData.ONTOLOGY_TERM_SYNONYM, getSynonymEntities(synonymIds));
				entity.set(OntologyTermMetaData.ONTOLOGY_TERM_DYNAMIC_ANNOTATION,
						getOntologyTermDynamicAnnotationEntities(annotationIds));
				entity.set(OntologyTermMetaData.ONTOLOGY, getOntology(ontologyIRI));

				return entity;
			}
		};
	}

	private List<Entity> getOntologyTermDynamicAnnotationEntities(Set<String> annotationIds)
	{
		Iterable<Entity> ontologyTermDynamicAnnotationEntities = dataService.findAll(
				OntologyTermDynamicAnnotationMetaData.ENTITY_NAME,
				new QueryImpl().in(OntologyTermDynamicAnnotationMetaData.ID, annotationIds));

		if (Iterables.size(ontologyTermDynamicAnnotationEntities) != annotationIds.size()) throw new IllegalArgumentException(
				"The expected number of synonym (" + annotationIds.size() + ") is different from actual size ("
						+ Iterables.size(ontologyTermDynamicAnnotationEntities) + ")");

		return Lists.newArrayList(ontologyTermDynamicAnnotationEntities);
	}

	private List<Entity> getSynonymEntities(Set<String> synonymIds)
	{
		Iterable<Entity> ontologyTermSynonymEntities = dataService.findAll(OntologyTermSynonymMetaData.ENTITY_NAME,
				new QueryImpl().in(OntologyMetaData.ID, synonymIds));

		if (Iterables.size(ontologyTermSynonymEntities) != synonymIds.size()) throw new IllegalArgumentException(
				"The expected number of synonym (" + synonymIds.size() + ") is different from actual size ("
						+ Iterables.size(ontologyTermSynonymEntities) + ")");

		return Lists.newArrayList(ontologyTermSynonymEntities);
	}

	private Entity getOntology(String ontologyIRI)
	{
		Entity ontologyEntity = dataService.findOne(OntologyMetaData.ENTITY_NAME,
				new QueryImpl().eq(OntologyMetaData.ONTOLOGY_IRI, ontologyIRI));

		if (ontologyEntity == null) throw new IllegalArgumentException("Ontology " + ontologyIRI
				+ " does not exist in the database!");

		return ontologyEntity;
	}

	@Override
	public void close() throws IOException
	{
		// Do nothing
	}

	@Override
	public String getName()
	{
		return OntologyTermMetaData.ENTITY_NAME;
	}

	@Override
	public EntityMetaData getEntityMetaData()
	{
		return OntologyTermMetaData.getEntityMetaData();
	}

	@Override
	public <E extends Entity> Iterable<E> iterator(Class<E> clazz)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public String getUrl()
	{
		throw new UnsupportedOperationException();
	}
}
