import db
from db.user_db import UserDb


class User:
    staff_users = ['staff', 'staff_only']

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

    def get_id(self):
        return self.db.get_id(email=self.email)

    def get_email(self, user_id):
        self.email = self.db.get_email(user_id=user_id)

    def exists(self):
        return self.db.user_exists(email=self.email)

    def create(self, password, affinity='', restaurant_id=''):
        user_id = self.db.generate_id()

        if not affinity:
            affinity = 'customer'

        return self.db.create_user(user_id=user_id,
                                   email=self.email,
                                   password=password,
                                   affinity=affinity,
                                   restaurant_id=restaurant_id)

    def get_my_restaurant(self, user_id):

        # This query is only valid if the user works at a restaurant.
        if self.get_affinity() not in self.staff_users:
            return ''

        return self.db.get_user_restaurant(user_id=user_id)
