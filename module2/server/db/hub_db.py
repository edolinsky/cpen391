import MySQLdb
from database import Database


class HubDb(Database):

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

    def hub_is_affiliated(self, hub_id, restaurant_id):
        exists = 0
        query = ("SELECT EXISTS(SELECT * FROM hubs "
                 "WHERE id = '{}' AND restaurant_id = '{}')AS HUB_AFFILIATED;").format(
            hub_id, restaurant_id
        )

        self.connect()
        try:
            self.cursor.execute(query)
            exists = self.cursor.fetchone()
        except MySQLdb.Error:
            print "Error: Unable to fetch data."
        self.close()

        if exists == 1:
            return True
        else:
            return False

    def get_hub_attendant_id(self, hub_id):
        attendant_id = ''
        query = "SELECT attendant FROM hub_attendant WHERE hub_id = '{}';".format(hub_id)

        self.connect()
        try:
            self.cursor.execute(query)
            attendant_id = self.cursor.fetchone()['attendant']
        except MySQLdb.Error:
            print "Error: Unable to fetch data."
        self.close()

        return attendant_id

    def get_table_name(self, hub_id):
        table_name = ''
        query = "SELECT table_name FROM hub_attendant WHERE hub_id = '{}';".format(hub_id)

        self.connect()
        try:
            self.cursor.execute(query)
            table_name = self.cursor.fetchone()['table_name']
        except MySQLdb.Error:
            print "Error: Unable to fetch data."
        self.close()

        return table_name

    def get_hub_restaurant_id(self, hub_id):
        restaurant_id = ''
        query = "SELECT restaurant_id FROM hubs WHERE id = '{}';".format(hub_id)

        self.connect()
        try:
            self.cursor.execute(query)
            result = self.cursor.fetchone()
            if result is not None:
                restaurant_id = result['restaurant_id']
        except MySQLdb.Error:
            print "Error: Unable to fetch data."
        self.close()

        return restaurant_id
