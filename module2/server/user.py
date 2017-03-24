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

    def update_app_id(self, app_id):
        return self.db.update_reg_id(email=self.email, reg_id=app_id)

    def get_app_id(self, user_id):
        return self.db.get_reg_id(user_id=user_id)

    def delete(self):
        response = ""

        if self.get_affinity() in self.staff_users:
            if self.remove_from_staff():
                response += "Removed from staff and "
            else:
                response += "Failed to delete user."
                return {'error': response}

        if self.db.delete_user(email=self.email):
            response += "deleted user."
        else:
            response += "Failed to delete user."

        return {'message': response}

    def remove_from_staff(self):
        user_id = self.get_id()
        return self.db.delete_from_staff(user_id=user_id)
