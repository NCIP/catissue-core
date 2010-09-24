update dyextn_role set ASSOCIATION_TYPE = 'CONTAINTMENT'
where identifier in
(
	select source_role_id from dyextn_association where identifier in
	(
		select de_association_id from intra_model_association where association_id =
		(
			select intermediate_path from path where first_entity_id =
			(
				select identifier from dyextn_abstract_metadata
				where name = 'edu.wustl.catissuecore.domain.SpecimenArrayContent'
			)
			and last_entity_id =
			(
				select identifier from dyextn_abstract_metadata
				where name = 'edu.wustl.catissuecore.domain.SpecimenArray'
			)
		)
	)
);