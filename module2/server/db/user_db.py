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

        user_pw = ''
        query = "SELECT password FROM user WHERE EMAIL = '{}';".format(email)
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

        user_affinity = ''
        query = "SELECT affinity FROM user WHERE EMAIL = '{}';".format(email)
        try:
            self.cursor.execute(query)
            user_affinity = self.cursor.fetchone()['affinity']
        except MySQLdb.Error:
            print "Error: Unable to fetch data."

        self.close()
        return user_affinity

    def user_exists(self, email):
        """
        Checks database to determine whether the user exists
        :param email:
        :return:
        """
        self.connect()
        exists = '0'

        query = "SELECT EXISTS(SELECT * FROM user where EMAIL = '{}')AS USER_EXISTS;".format(email)
        try:
            self.cursor.execute(query)
            exists = self.cursor.fetchone()['USER_EXISTS']
        except MySQLdb.Error:
            print "Error: Unable to fetch data."
        self.close()

        if exists == '1':
            return True
        else:
            return False

    def create_user(self, user_id, email, password, affinity='', restaurant_id=''):
        """
        Creates a new user with the given parameters. This assumes that steps have been taken to
        ensure that the user does not already exist, and that the restaurant_id exists.
        :param user_id:
        :param email:
        :param password:
        :param affinity:
        :param restaurant_id:
        :return:
        """
        if not affinity:
            affinity = 'customer'

        # Create the user
        query = "INSERT INTO user (id, email, password, affinity) VALUES ('{}', '{}', '{}', '{}');".format(
            user_id, email, password, affinity
        )

        self.connect()
        try:
            self.cursor.execute(query)
        except MySQLdb.Error:
            self.conn.rollback()
            self.close()

        # If the user is staff, add the user-restaurant ID pair to the restaurant_staff table
        if affinity == 'staff':
            query = "INSERT INTO restaurant_staff (user_id, restaurant_id) VALUES ('{}', '{}');".format(
                user_id, restaurant_id
            )
            try:
                self.cursor.execute(query)
            except MySQLdb.Error:
                self.conn.rollback()
                self.close()

        self.conn.commit()
        self.close()


