import uuid

import db
from db.user_db import UserDb


class User:
    def __init__(self, email):
        self.email = email
        self.db = UserDb(user=db.db_user,
                         passwd=db.db_passwd,
                         host=db.db_host,
                         db=db.db_database,
                         port=db.db_port)

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
        user_id = self.generate_id()

        if not affinity:
            affinity = 'customer'

        return self.db.create_user(user_id=user_id,
                                   email=self.email,
                                   password=password,
                                   affinity=affinity,
                                   restaurant_id=restaurant_id)

    @staticmethod
    def generate_id():
        return str(uuid.uuid4().hex)[-10:]
