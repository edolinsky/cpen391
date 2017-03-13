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
        Database.connect(self)

    def get_restaurant(self, name):
        pass

    def insert_restaurant(self, name):
        pass

    def restaurant_exists(self, restaurant_id):

        exists = '0'
        query = "SELECT EXISTS(SELECT * FROM restaurants where restaurant_id = '{}')AS RESTAURANT_EXISTS;".format(
            restaurant_id
        )

        self.connect()
        try:
            self.cursor.execute(query)
            exists = self.cursor.fetchone()['RESTAURANT_EXISTS']
        except MySQLdb.Error:
            print "Error: Unable to fetch data."
        self.close()

        if exists == '1':
            return True
        else:
            return False
