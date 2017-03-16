import MySQLdb
import uuid


class Database:
    def __init__(self, user, passwd, host, db, port=3306):
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
                                        passwd=self.passwd,
                                        host=self.host,
                                        db=self.db,
                                        port=self.port)

            self.cursor = self.conn.cursor(cursorclass=MySQLdb.cursors.DictCursor)
        except MySQLdb.Error as e:
            print "DB Connection failed: {}".format(e)

    def close(self):
        """
        Close a connection to a database
        :return:
        """
        if self.cursor:
            self.cursor.close()

        if self.conn:
            self.conn.close()

    @staticmethod
    def generate_id():
        return str(uuid.uuid4().hex)[-10:]
