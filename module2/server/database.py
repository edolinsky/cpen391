import MySQLdb


class Database:

    def __init__(self, user, passwd, host, db, port='3306'):
        self.user = user
        self.passwd = passwd
        self.host = host
        self.db = db
        self.port = port
        self.conn = None

    def connect(self):
        """
        Connect to a database
        :return:
        """

        try:
            self.conn = MySQLdb.connect(user=self.user,
                                        password=self.passwd,
                                        host=self.host,
                                        db=self.db,
                                        port=self.port)
        except MySQLdb.Error:
            self.conn.rollback()
            print "DB Connection failed."

    def close(self):
        """
        Close a connection to a database
        :return:
        """

        if self.conn:
            self.conn.close()

    def get_user_password_by_email(self, email):
        self.connect()

        cursor = self.conn.cursor()

        query = ("SELECT PASSWORD from user WHERE EMAIL = '{}'".format(email))
        cursor.execute(query)

        user_pw = cursor.PASSWORD

        self.close()

        return user_pw

    def user_exists(self, email):
        pass
