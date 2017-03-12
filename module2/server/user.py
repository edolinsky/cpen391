import uuid

import server
from db.user_db import UserDb


class User:
    def __init__(self, email):
        self.email = email
        self.db = UserDb(user=server.db_user,
                         passwd=server.db_passwd,
                         host=server.db_host,
                         db=server.db_database,
                         port=server.db_port)

    def is_valid(self, passwd):

        if passwd == self.db.get_user_password(self.email):
            return True
        else:
            return False

    def get_affinity(self):

        return self.db.get_affinity(email=self.email)

    def exists(self):
        return self.db.user_exists(email=self.email)

    def create(self, password, affinity='', restaurant_id=''):
        user_id = generate_id()

        self.db.create_user(user_id=user_id,
                            email=self.email,
                            password=password,
                            affinity=affinity,
                            restaurant_id=restaurant_id)


def generate_id():
    return uuid.uuid4()[:-10]
