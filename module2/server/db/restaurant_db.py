import MySQLdb
from database import Database


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

    def insert_restaurant(self, name):
        # insert in restaurant table
        # create table with name 'restaurant_id'
        pass

    def restaurant_exists(self, restaurant_id):

        exists = 0
        query = "SELECT EXISTS(SELECT * FROM restaurants where id = '{}')AS RESTAURANT_EXISTS;".format(
            restaurant_id
        )

        self.connect()
        try:
            self.cursor.execute(query)
            exists = self.cursor.fetchone()['RESTAURANT_EXISTS']
        except MySQLdb.Error:
            print "Error: Unable to fetch data."
        self.close()

        if exists == 1:
            return True
        else:
            return False

    def select_all_open_orders(self, restaurant_id):

        query = ("SELECT o.id, o.customer_name, o.status, m.name, m.id as menu_id, o.table_id FROM {} o "
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
