import db
from db.user_db import UserDb


class User:
    user_affinities = ['customer', 'staff', 'staff_only']
    staff_users = ['staff', 'staff_only']

    def __init__(self, email):
        self.email = email
        self.db = UserDb(user=db.db_user,
                         passwd=db.db_passwd,
                         host=db.db_host,
                         db=db.db_database,
                         port=db.db_port)

    def is_valid(self, passwd):
        """
        Determine whether the specified password matches that which is
        stored in the database for this user.
        :param passwd: User password.
        :return: boolean; true if specified password matches.
        """

        if passwd == self.db.get_user_password(self.email):
            return True
        else:
            return False

    def get_affinity(self):
        """
        Retrieve this user's affinity. Valid affinities can be
        found in user.user_affinities.
        :return: String denoting user affinity.
        """
        return self.db.get_affinity(email=self.email)

    def get_id(self):
        """
        Retrieve this user's user ID.
        :return: User ID string.
        """
        return self.db.get_id(email=self.email)

    def get_email(self, user_id):
        """
        Retrieve a user's email address given their user ID.
        :param user_id: User ID string.
        :return: User email string.
        """
        self.email = self.db.get_email(user_id=user_id)

    def exists(self):
        """
        Determine whether this user exists in the database.
        :return: Boolean; true if user email exists in database.
        """
        return self.db.user_exists(email=self.email)

    def create(self, password, affinity='', restaurant_id=''):
        """
        Create a user entity in the database.
        :param password: User password.
        :param affinity: User affinity.
        :param restaurant_id: Existing restaurant ID. Required only if
            creating a non-customer user.
        :return: Response JSON object.
        """
        user_id = self.db.generate_id()

        if not affinity:
            affinity = 'customer'

        return self.db.create_user(user_id=user_id,
                                   email=self.email,
                                   password=password,
                                   affinity=affinity,
                                   restaurant_id=restaurant_id)

    def get_my_restaurant(self, user_id):
        """
        Get the restaurant at which the user works.
        :param user_id: Unique user ID.
        :return: Empty string if the user is customer-only, or if the
            user does not work for a registered restaurant. Otherwise,
            the restaurant ID to which the user is registered.
        """
        # This query is only valid if the user works at a restaurant.
        if self.get_affinity() not in self.staff_users:
            return ''

        return self.db.get_user_restaurant(user_id=user_id)

    def update_app_id(self, app_id):
        """
        Updates this user's firebase cloud messaging identifier.
        :param app_id: FCM ID String
        :return: Response JSON body.
        """
        return self.db.update_reg_id(email=self.email, reg_id=app_id)

    def get_app_id(self, user_id):
        """
        Retrieve this user's firebase cloud messaging identifier.
        :param user_id: User ID string.
        :return: FCM ID string.
        """
        return self.db.get_reg_id(user_id=user_id)

    def delete(self):
        """
        Delete a user from the database. Also deletes the user from
        the restaurant_staff table if the user is staff.
        :return: Response JSON body.
        """
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
        """
        Remove a user from the restaurant_staff table.
        :return:
        """
        user_id = self.get_id()
        return self.db.delete_from_staff(user_id=user_id)
