classDiagram
direction BT
class category {
   varchar(255) code
   varchar(255) name
   text description
   bigint parent_id
   boolean allow_multiple_categories
   boolean inherit_features
   boolean active
   integer sequence
   varchar(255) created_by
   timestamp created_date
   varchar(255) last_modified_by
   timestamp last_modified_date
   varchar(31) dtype
   bigint id
}
class category_feature_template {
   bigint category_id
   varchar(255) code
   varchar(255) name
   text description
   varchar(50) feature_type
   varchar(50) attribute_type
   varchar(255) validation_pattern
   numeric min_value
   numeric max_value
   text allowed_values
   varchar(50) unit
   boolean visible
   boolean editable
   boolean searchable
   boolean comparable
   boolean mandatory
   boolean multi_valued
   jsonb metadata
   integer display_order
   boolean is_required
   varchar(255) created_by
   timestamp created_date
   varchar(255) last_modified_by
   timestamp last_modified_date
   bigint id
}
class class_attribute_assignments {
   bigint classification_class_id
   bigint classification_attribute_id
   varchar(50) unit
   varchar(50) attribute_type
   boolean mandatory
   boolean multi_valued
   integer sequence
   varchar(255) created_by
   timestamp created_date
   varchar(255) last_modified_by
   timestamp last_modified_date
   bigint id
}
class classification_attribute {
   varchar(255) code
   varchar(255) name
   text description
   varchar(50) attribute_type
   varchar(255) validation_pattern
   numeric min_value
   numeric max_value
   varchar(50) unit
   boolean visible
   boolean editable
   boolean searchable
   boolean comparable
   boolean mandatory
   boolean multi_valued
   jsonb metadata
   varchar(255) created_by
   timestamp created_date
   varchar(255) last_modified_by
   timestamp last_modified_date
   bigint id
}
class classification_attribute_value {
   bigint assignment_id
   text value
   varchar(255) created_by
   timestamp created_date
   varchar(255) last_modified_by
   timestamp last_modified_date
   bigint id
}
class classification_class {
   varchar(255) created_by
   timestamp created_date
   varchar(255) last_modified_by
   timestamp last_modified_date
   bigint id
}
class classification_class_metadata {
   text value
   bigint class_id
   varchar(255) key
}
class enum_value {
   varchar(255) code
   integer sort_order
   varchar(255) created_by
   timestamp created_date
   varchar(255) last_modified_by
   timestamp last_modified_date
   bigint id
}
class enum_value_translation {
   bigint enum_value_id
   varchar(10) language_code
   varchar(255) value
   varchar(255) created_by
   timestamp created_date
   varchar(255) last_modified_by
   timestamp last_modified_date
   bigint id
}
class feature_value_event {
   bigint feature_id
   bigint product_id
   text old_value
   text new_value
   varchar(50) event_type
   text metadata
   timestamp timestamp
   varchar(255) created_by
   timestamp created_date
   varchar(255) last_modified_by
   timestamp last_modified_date
   bigint id
}
class product {
   varchar(255) code
   varchar(255) name
   text description
   varchar(50) product_type
   varchar(50) status
   jsonb metadata
   varchar(255) sku
   varchar(255) created_by
   timestamp created_date
   varchar(255) last_modified_by
   timestamp last_modified_date
   bigint id
}
class product_categories {
   bigint product_id
   bigint category_id
}
class product_feature {
   bigint product_id
   bigint template_id
   varchar(255) code
   varchar(255) name
   text description
   varchar(50) feature_type
   varchar(50) attribute_type
   varchar(255) validation_pattern
   varchar(255) min_value
   varchar(255) max_value
   text allowed_values
   varchar(255) default_value
   bigint unit_id
   jsonb metadata
   boolean required
   boolean visible
   boolean editable
   boolean searchable
   boolean comparable
   boolean multi_valued
   text values
   varchar(255) created_by
   timestamp created_date
   varchar(255) last_modified_by
   timestamp last_modified_date
   bigint id
}
class product_feature_value {
   bigint product_id
   bigint feature_id
   bigint template_id
   varchar(50) type
   varchar(50) unit
   varchar(50) unit_of_measure
   varchar(50) status
   varchar(50) validation_status
   varchar(255) validation_pattern
   text validation_message
   jsonb attribute_values
   varchar(255) created_by
   timestamp created_date
   varchar(255) last_modified_by
   timestamp last_modified_date
   bigint id
}
class unit {
   varchar(50) code
   varchar(255) name
   text description
   timestamp created_date
   timestamp last_modified_date
   bigint id
}
class unit_of_measure {
   varchar(50) code
   varchar(255) name
   text description
   varchar(50) base_unit
   numeric conversion_factor
   varchar(255) created_by
   timestamp created_date
   varchar(255) last_modified_by
   timestamp last_modified_date
   bigint id
}

category  -->  category : parent_id:id
category_feature_template  -->  category : category_id:id
class_attribute_assignments  -->  category : classification_class_id:id
class_attribute_assignments  -->  classification_attribute : classification_attribute_id:id
class_attribute_assignments  -->  classification_class : classification_class_id:id
classification_attribute_value  -->  class_attribute_assignments : assignment_id:id
classification_class  -->  category : id
classification_class_metadata  -->  category : class_id:id
enum_value_translation  -->  enum_value : enum_value_id:id
feature_value_event  -->  product : product_id:id
feature_value_event  -->  product_feature : feature_id:id
product_categories  -->  category : category_id:id
product_categories  -->  product : product_id:id
product_feature  -->  category_feature_template : template_id:id
product_feature  -->  product : product_id:id
product_feature  -->  unit : unit_id:id
product_feature_value  -->  product : product_id:id
product_feature_value  -->  product_feature : feature_id:id
