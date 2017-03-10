import server
from db.user_db import UserDb


class User:
    def __init__(self, email):
        self.id = email
        self.db = UserDb(user=server.db_user,
                         passwd=server.db_passwd,
                         host=server.db_host,
                         db=server.db_database,
                         port=server.db_port)

    def is_valid(self, passwd):

        if passwd == self.db.get_user_password(self.id):
            return True
        else:
            return False

    def get_affinity(self):

        return self.db.get_affinity(email=self.id)
