import MySQLdb
from database import Database


class UserDb(Database):
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

    def get_user_password(self, email):
        """
        Retrieve a user's password given their unique email address.
        :param email:
        :return:
        """
        self.connect()
        self.cursor = self.conn.cursor()

        user_pw = ''
        query = "SELECT password FROM user WHERE EMAIL = '{}'".format(email)
        try:
            self.cursor.execute(query)
            user_pw = self.cursor.fetchone()
        except MySQLdb.Error:
            print "Error: Unable to fetch data."

        self.close()
        return user_pw

    def get_affinity(self, email):
        """
        Retrieve a user's affinity given their unique email address.
        :param email:
        :return:
        """
        self.connect()
        self.cursor = self.conn.cursor

        user_affinity = ''
        query = "SELECT affinity FROM user WHERE EMAIL = '{}'".format(email)
        try:
            self.cursor.execute(query)
            user_affinity = self.cursor.fetchone()
        except MySQLdb.Error:
            print "Error: Unable to fetch data."

        self.close()
        return user_affinity
