import MySQLdb
from database import Database


class MenuDb(Database):
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
        Database.connect(self)

    def get_menu(self, restaurant_id):
        pass

    def get_sub_menu(self, restaurant_id, item_type):
        pass
