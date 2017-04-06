import MySQLdb
from database import Database
from create_table import CreateTable


class RestaurantDb(Database):
    def __init__(self, user, passwd, host, db, port=3306):
        Database.__init__(self,
                          user=user,
                          passwd=passwd,
                          host=host,
                          db=db,
                          port=port)

    def connect(self):
        Database.connect(self)

    def close(self):
        Database.close(self)

    def get_restaurant(self, name):
        pass

    def create_restaurant(self, restaurant_id, name):
        tablecreator = CreateTable()
        query = "INSERT INTO restaurants (id, name) VALUES (\"{}\", \"{}\");".format(restaurant_id, name)
        self.connect()

        try:
            self.cursor.execute(query)
        except MySQLdb.Error as e:
            print "Error: Unable to insert data: {}".format(e)
            self.conn.rollback()
            self.close()
            return {'error': 'Unable to register restaurant.'}

        create_query = tablecreator.create_restaurant_table(restaurant_id=restaurant_id)

        try:
            self.cursor.execute(create_query)
        except MySQLdb.Error:
            print "Error: Unable to create restaurant."
            self.conn.rollback()
            self.close()
            return {'error': 'Unable to create restaurant.'}

        self.conn.commit()
        return {'restaurant_id': restaurant_id,
                'name': name,
                'message': 'Restaurant Created.'}

    def restaurant_exists(self, restaurant_id):

        exists = 0
        query = "SELECT EXISTS(SELECT * FROM restaurants where id = '{}')AS RESTAURANT_EXISTS;".format(
            restaurant_id
        )

        self.connect()
        try:
            self.cursor.execute(query)
            result = self.cursor.fetchone()
            if result is not None:
                exists = result['RESTAURANT_EXISTS']
        except MySQLdb.Error:
            print "Error: Unable to fetch data."
        self.close()

        if exists == 1:
            return True
        else:
            return False

    def select_all_open_orders(self, restaurant_id):

        query = ("SELECT o.id, o.order_id, o.customer_name, o.status, m.name, m.id as menu_id, o.table_id FROM {} o "
                 "JOIN menu m ON m.id = o.menu_id "
                 "WHERE o.status NOT IN ('served','complete', 'cancelled');").format(restaurant_id)

        orders_info = {'restaurant_id': restaurant_id}

        self.connect()
        try:
            self.cursor.execute(query)
            orders = self.cursor.fetchall()
            orders_info.update({'orders': orders})
        except MySQLdb.Error as e:
            print "Unable to fetch data: {}".format(e)
            orders_info.update({'error': 'Unable to fetch order data.'})
        self.close()

        return orders_info

    def get_staff_hub_mappings(self, restaurant_id):
        mappings = {}
        query = ("SELECT h.id AS table_id, ha.table_name, u.id AS attendant_id, u.email AS email FROM hubs h "
                 "JOIN hub_attendant ha on h.id = ha.hub_id "
                 "JOIN user u ON u.id = ha.attendant "
                 "WHERE h.restaurant_id = '{}';").format(restaurant_id)

        self.connect()
        try:
            self.cursor.execute(query)
            result = self.cursor.fetchall()
            mappings.update({'mappings': result})
        except MySQLdb.Error as e:
            print "Unable to fetch data: {}".format(e)
            mappings.update({'error': 'Unable to fetch mapping data.'})
        self.close()

        return mappings

    def update_staff_hub_mappings(self, mapping_info):
        mapping_tuples = []
        for mapping in mapping_info['mappings']:
            mapping_tuples.append((mapping['attendant_id'], mapping['table_id']))

        query = "UPDATE hub_attendant SET attendant = %s WHERE hub_id = %s;"
        self.connect()

        try:
            self.cursor.executemany(query=query, args=mapping_tuples)
        except MySQLdb.Error as e:
            print "Unable to update data: {}".format(e)
            self.conn.rollback()
            self.close()

            mapping_info.update({'error': 'Failed to submit new mappings.'})
            return mapping_info

        self.conn.commit()
        self.close()

        return mapping_info

    def remove_staff_from_mappings(self, user_id):

        query = "UPDATE hub_attendant SET attendant = NULL WHERE attendant = '{}'".format(user_id)

        self.connect()
        try:
            self.cursor.execute(query)
        except MySQLdb.Error as e:
            print "Unable to update data: {}".format(e)
            self.conn.rollback()
            self.close()
            return {'error': 'Failed to remove user from table mappings.'}

        self.conn.commit()
        self.close()
        return {'message': 'Removed user from table mappings.'}
