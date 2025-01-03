classDiagram
direction BT
class business {
   varchar(255) code
   varchar(255) name
   text description
   varchar(255) created_by
   timestamp created_date
   varchar(255) last_modified_by
   timestamp last_modified_date
   uuid id
}
class catalog {
   varchar(255) code
   uuid business_id
   varchar(255) name
   text description
   varchar(255) created_by
   timestamp created_date
   varchar(255) last_modified_by
   timestamp last_modified_date
   uuid id
}
class catalog_category {
   boolean primary_category
   varchar(255) created_by
   timestamp created_date
   varchar(255) last_modified_by
   timestamp last_modified_date
   uuid catalog_id
   uuid category_id
}
class category {
   varchar(50) dtype
   uuid business_id
   uuid catalog_id
   uuid parent_id
   varchar(255) name
   boolean inherit_features
   text description
   varchar(255) code
   integer level
   varchar(50) status
   jsonb metadata
   varchar(255) created_by
   timestamp created_date
   varchar(255) last_modified_by
   timestamp last_modified_date
   uuid id
}
class category_feature_template {
   varchar(255) name
   text description
   varchar(255) code
   varchar(50) feature_type
   varchar(255) validation_pattern
   varchar(255) min_value
   varchar(255) max_value
   text allowed_values
   varchar(50) attribute_type
   boolean comparable
   boolean visible
   boolean searchable
   boolean editable
   boolean multi_valued
   varchar(255) default_value
   uuid unit_id
   jsonb metadata
   boolean required
   uuid category_id
   varchar(255) created_by
   timestamp created_date
   varchar(255) last_modified_by
   timestamp last_modified_date
   uuid id
}
class product {
   uuid business_id
   uuid catalog_id
   varchar(255) name
   text description
   varchar(255) code
   varchar(50) product_type
   varchar(50) status
   jsonb metadata
   varchar(255) sku
   numeric(19,4) price
   uuid unit_of_measure_id
   varchar(255) created_by
   timestamp created_date
   varchar(255) last_modified_by
   timestamp last_modified_date
   uuid id
}
class product_attribute {
   timestamp(6) created_date
   timestamp(6) last_modified_date
   uuid product_id
   varchar(255) attribute_name
   varchar(255) attribute_value
   varchar(255) created_by
   varchar(255) last_modified_by
   varchar(255) unit_of_measure
   uuid id
}
class product_categories {
   boolean primary_category
   varchar(255) created_by
   timestamp created_date
   varchar(255) last_modified_by
   timestamp last_modified_date
   uuid product_id
   uuid category_id
}
class product_feature {
   varchar(255) name
   text description
   varchar(255) code
   varchar(50) feature_type
   varchar(255) validation_pattern
   varchar(255) min_value
   varchar(255) max_value
   text allowed_values
   varchar(50) attribute_type
   boolean comparable
   boolean visible
   boolean searchable
   boolean editable
   boolean multi_valued
   varchar(255) default_value
   uuid unit_id
   jsonb metadata
   boolean required
   uuid product_id
   uuid template_id
   varchar(255) created_by
   timestamp created_date
   varchar(255) last_modified_by
   timestamp last_modified_date
   uuid id
}
class product_feature_value {
   varchar(50) type
   uuid product_id
   jsonb attribute_values
   jsonb metadata
   varchar(50) status
   uuid template_id
   uuid feature_id
   varchar(255) unit
   varchar(255) unit_of_measure
   varchar(255) validation_message
   varchar(255) validation_pattern
   varchar(50) validation_status
   text value
   varchar(255) created_by
   timestamp created_date
   varchar(255) last_modified_by
   timestamp last_modified_date
   uuid id
}
class site {
   uuid business_id
   varchar(255) name
   varchar(255) domain
   varchar(10) locale
   varchar(3) currency
   varchar(255) created_by
   timestamp created_date
   varchar(255) last_modified_by
   timestamp last_modified_date
   uuid id
}
class site_catalog {
   boolean is_default
   varchar(255) created_by
   timestamp created_date
   varchar(255) last_modified_by
   timestamp last_modified_date
   uuid site_id
   uuid catalog_id
}
class unit {
   varchar(10) symbol
   varchar(255) code
   varchar(255) name
   varchar(100) type
   text description
   varchar(255) created_by
   timestamp created_date
   varchar(255) last_modified_by
   timestamp last_modified_date
   uuid id
}
class unit_of_measure {
   varchar(255) code
   varchar(255) name
   varchar(100) type
   boolean active
   varchar(10) display_symbol
   varchar(255) metadata
   text description
   uuid unit_id
   numeric(19,4) conversion_factor
   uuid base_unit_id
   varchar(255) created_by
   timestamp created_date
   varchar(255) last_modified_by
   timestamp last_modified_date
   uuid id
}

catalog  -->  business : business_id:id
catalog_category  -->  catalog : catalog_id:id
catalog_category  -->  category : category_id:id
category  -->  business : business_id:id
category  -->  catalog : catalog_id:id
category  -->  category : parent_id:id
category_feature_template  -->  category : category_id:id
category_feature_template  -->  unit : unit_id:id
product  -->  business : business_id:id
product  -->  catalog : catalog_id:id
product  -->  unit_of_measure : unit_of_measure_id:id
product_attribute  -->  product : product_id:id
product_categories  -->  category : category_id:id
product_categories  -->  product : product_id:id
product_feature  -->  category_feature_template : template_id:id
product_feature  -->  product : product_id:id
product_feature  -->  unit : unit_id:id
product_feature_value  -->  category_feature_template : template_id:id
product_feature_value  -->  product : product_id:id
product_feature_value  -->  product_feature : feature_id:id
site  -->  business : business_id:id
site_catalog  -->  catalog : catalog_id:id
site_catalog  -->  site : site_id:id
unit_of_measure  -->  unit : unit_id:id
unit_of_measure  -->  unit_of_measure : base_unit_id:id
