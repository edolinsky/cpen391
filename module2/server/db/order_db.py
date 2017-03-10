import MySQLdb
from database import Database


class OrderDb(Database):
    def __init__(self, user, passwd, host, db, port='3306'):
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

    def insert_orders(self, restaurant_id, table_id, order_id, order):
        # todo: implement
        pass

    def get_orders(self, restaurant_id, table_id, order_id):
        # todo: implement
        # "SELECT * FROM '{}' WHERE tableid and orderid, join menu"
        pass

    def update_orders(self, restaurant_id, table_id, order_id, order):
        # todo: implement
        # "INSERT ON DUPLICATE KEY UPDATE"
        pass
