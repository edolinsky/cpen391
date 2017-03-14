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

    def select_order(self, order_id):
        pass

    def insert_order(self, order_tuple):
        self.connect()

        order_info = {}

        query = ("INSERT INTO {} (id, menu_id, table_id, order_id, customer_id, customer, status) "
                 "VALUES (%s, %s, %s, %s, %s, %s, %s);")

        try:
            self.cursor.executemany(query=query, args=order_tuple)
        except MySQLdb.Error:
            print "Unable to update data."
            self.conn.rollback()
            self.close()
            order_info.update({'error': 'Failed to submit order.'})
            return order_info

        self.conn.commit()
        self.close()

        return order_info

    def get_order(self, restaurant_id, table_id, order_id):
        # todo: implement
        # "SELECT * FROM '{}' WHERE tableid and orderid, join menu"
        pass

    def update_order(self, restaurant_id, table_id, order_id, order):
        # todo: implement
        # "INSERT ON DUPLICATE KEY UPDATE"
        pass
