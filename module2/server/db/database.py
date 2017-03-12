import MySQLdb


class Database:
    def __init__(self, user, passwd, host, db, port='3306'):
        self.user = user
        self.passwd = passwd
        self.host = host
        self.db = db
        self.port = port
        self.conn = None
        self.cursor = None

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

            self.cursor = self.conn.cursor
        except MySQLdb.Error:
            print "DB Connection failed."

    def close(self):
        """
        Close a connection to a database
        :return:
        """
        if self.cursor:
            self.cursor.close()

        if self.conn:
            self.conn.close()
