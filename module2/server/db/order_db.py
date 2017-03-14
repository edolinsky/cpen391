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
            order_tuples.append((item['id'], item['menu_id'], order_info['table_id'], order_info['order_id'],
                                 order_info['customer_id'], item['customer_name'], item['status']))

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

    def get_order(self, order_id):
        # todo: implement
        # "SELECT * FROM '{}' WHERE tableid and orderid, join menu"
        pass

    def update_order(self, restaurant_id, table_id, order_id, order):
        # todo: implement
        # "INSERT ON DUPLICATE KEY UPDATE"
        pass

    def update_status(self):
        # todo: implement
        # This should be the only
        pass
