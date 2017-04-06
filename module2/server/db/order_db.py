import MySQLdb
from database import Database


class OrderDb(Database):
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

    def insert_order(self, order_info):
        """
        Inserts a brand new set of items in an orderd
        :param order_info:
        :return:
        """
        # Prepare list of tuples from specified list of dicts.
        order_tuples = []
        for item in order_info['items']:
            order_tuples.append((item['id'], item['menu_id'], order_info['table_id'],
                                 order_info['order_id'], order_info['customer_id'],
                                 item['customer_name'], item['status']))

        self.connect()

        query = ("INSERT INTO {} (id, menu_id, table_id, order_id, customer_id, customer_name, status) "
                 "VALUES (%s, %s, %s, %s, %s, %s, %s);").format(order_info['restaurant_id'])

        try:
            self.cursor.executemany(query=query, args=order_tuples)
        except MySQLdb.Error as e:
            print "Unable to update data: {}".format(e)
            self.conn.rollback()
            self.close()

            # Add error message and remove any information that is now invalid.
            order_info.update({'error': 'Failed to submit order.'})
            print order_info
            order_info.pop('items')
            order_info.pop('order_id')
            return order_info

        self.conn.commit()
        self.close()

        return order_info

    def get_order(self, restaurant_id, order_id):

        query = ("SELECT o.id, m.name, m.id as menu_id, m.price, m.description, m.type, o.customer_name, o.status "
                 "from {} o "
                 "JOIN menu m ON m.id = o.menu_id "
                 "WHERE o.order_id = '{}';").format(restaurant_id, order_id)

        order_info = {'restaurant_id': restaurant_id, 'order_id': order_id}
        self.connect()
        try:
            self.cursor.execute(query=query)
            order_items = self.cursor.fetchall()
            order_info.update({'items': order_items})
        except MySQLdb.Error as e:
            print "Unable to fetch data: {}".format(e)
            order_info.update({'error': 'Unable to fetch order information.'})

        self.close()
        return order_info

    def update_order(self, restaurant_id, table_id, order_id, order):
        # todo: implement
        # "INSERT ON DUPLICATE KEY UPDATE"
        pass

    def update_status(self, update_info):

        query = "UPDATE {} SET status = %s WHERE id = %s;".format(update_info['restaurant_id'])

        update_tuples = []
        for update in update_info['items']:
            update_tuples.append((update['status'], update['id']))

        self.connect()

        try:
            self.cursor.executemany(query=query, args=update_tuples)
        except MySQLdb.Error as e:
            print "Unable to update data: {}".format(e)
            self.conn.rollback()
            self.close()

            update_info.update({'error': 'Failed to update orders.'})
            return update_info

        self.conn.commit()
        self.close()
        return update_info

    def get_customer_id(self, order_id, restaurant_id):
        self.connect()

        customer_id = ''
        query = ("SELECT customer_id FROM {} "
                 "WHERE order_id = '{}';").format(restaurant_id, order_id)
        try:
            self.cursor.execute(query)
            result = self.cursor.fetchone()
            if result is not None:
                customer_id = result['customer_id']
        except MySQLdb.Error:
            print "Error: Unable to fetch data."

        self.close()
        return customer_id
