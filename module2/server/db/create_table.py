class CreateTable:
    hubs_create = ("CREATE TABLE `hubs` ("
                   "`id` varchar(18) NOT NULL,"
                   "`restaurant_id` varchar(10) NOT NULL,"
                   "`attendant` varchar(10) DEFAULT NULL,"
                   "`table_name` tinytext,"
                   "`fw_version` tinytext,"
                   "`hw_version` tinytext,"
                   "PRIMARY KEY (`id`),"
                   "UNIQUE KEY `table_name_id_uindex` (`id`)"
                   ") ENGINE=InnoDB DEFAULT CHARSET=utf8")

    menu_create = ("CREATE TABLE `menu` ("
                   "`id` varchar(10) NOT NULL,"
                   "`restaurant_id` varchar(10) NOT NULL,"
                   "`name` text NOT NULL,"
                   "`type` enum('drink','alcoholic','appetizer','entree','dessert','merchandise') DEFAULT NULL,"
                   "`description` longtext,"
                   "`price` float(10,2) DEFAULT NULL,"
                   "UNIQUE KEY `menu_id_uindex` (`id`)"
                   ") ENGINE=InnoDB DEFAULT CHARSET=utf8")

    restaurant_staff_create = ("CREATE TABLE `restaurant_staff` ("
                               "`user_id` varchar(10) NOT NULL,"
                               "`restaurant_id` varchar(10) NOT NULL,"
                               "`id` varchar(10) NOT NULL,"
                               "PRIMARY KEY (`id`),"
                               "UNIQUE KEY `restaurant_staff_id_uindex` (`id`)"
                               ") ENGINE=InnoDB DEFAULT CHARSET=utf8")

    restaurants_create = ("CREATE TABLE `restaurants` ("
                          "`id` varchar(10) NOT NULL,"
                          "`name` text NOT NULL,"
                          "PRIMARY KEY (`id`),"
                          "UNIQUE KEY `restaurants_id_uindex` (`id`)"
                          ") ENGINE=InnoDB DEFAULT CHARSET=utf8")

    orders_create = ("CREATE TABLE `{}` ("
                     "`id` varchar(10) NOT NULL,"
                     "`menu_id` varchar(10) NOT NULL,"
                     "`order_id` varchar(10) NOT NULL,"
                     "`table_id` varchar(18) NOT NULL,"
                     "`customer_name` tinytext,"
                     "`customer_id` varchar(10) DEFAULT NULL,"
                     "`status` enum('placed','prep','ready','served','complete','cancelled') DEFAULT NULL,"
                     "PRIMARY KEY (`id`)"
                     ") ENGINE=InnoDB DEFAULT CHARSET=utf8")

    user_create = ("CREATE TABLE `user` ("
                   "`id` varchar(10) NOT NULL,"
                   "`email` tinytext NOT NULL,"
                   "`password` longblob NOT NULL,"
                   "`affinity` enum('customer','staff','staff_only') NOT NULL DEFAULT 'customer',"
                   "`android_reg_id` text,"
                   "UNIQUE KEY `user_id_uindex` (`id`)"
                   ") ENGINE=InnoDB DEFAULT CHARSET=utf8")

    def __init__(self):
        pass

    @staticmethod
    def create_restaurant_table(restaurant_id):
        return CreateTable.orders_create.format(restaurant_id)
