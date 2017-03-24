import MySQLdb
from database import Database


class UserDb(Database):
    def __init__(self, user, passwd, host, db, port=3306):
        Database.__init__(self,
                          user=user,
                          passwd=passwd,
                          host=host,
                          db=db,
                          port=port)

    def connect(self):
        Database.connect(self)

    def close(self):
        Database.close(self)

    def get_user_password(self, email):
        """
        Retrieve a user's password given their unique email address.
        :param email:
        :return:
        """
        self.connect()

        user_pw = ''
        query = "SELECT password FROM user WHERE email = '{}';".format(email)
        try:
            self.cursor.execute(query)
            user_pw = self.cursor.fetchone()['password']
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
            result = self.cursor.fetchone()
            if result is not None:
                user_affinity = result['affinity']
        except MySQLdb.Error:
            print "Error: Unable to fetch data."

        self.close()
        return user_affinity

    def get_id(self, email):
        """
        Retrieve a user's ID given their unique email address.
        :param email:
        :return:
        """
        self.connect()

        user_id = ''
        query = "SELECT id FROM user WHERE EMAIL = '{}';".format(email)
        try:
            self.cursor.execute(query)
            result = self.cursor.fetchone()
            if result is not None:
                user_id = result['id']
        except MySQLdb.Error:
            print "Error: Unable to fetch data."

        self.close()
        return user_id

    def get_email(self, user_id):
        """
        Retrieve a user's email address given a unique user ID.
        :param user_id:
        :return:
        """
        self.connect()

        user_email = ''
        query = "SELECT email FROM user WHERE id = '{}';".format(user_id)
        try:
            self.cursor.execute(query)
            user_email = self.cursor.fetchone()['email']
        except MySQLdb.Error:
            print "Error: Unable to fetch data."

        self.close()
        return user_email

    def user_exists(self, email):
        """
        Checks database to determine whether the user exists
        :param email:
        :return:
        """
        self.connect()
        exists = 0

        query = "SELECT EXISTS(SELECT * FROM user where email = '{}')AS USER_EXISTS;".format(email)
        try:
            self.cursor.execute(query)
            exists = self.cursor.fetchone()['USER_EXISTS']
        except MySQLdb.Error:
            print "Error: Unable to fetch data."
        self.close()

        if exists == 1:
            return True
        else:
            return False

    def create_user(self, user_id, email, password, affinity, restaurant_id=''):
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

        user_info = {'email': email, 'affinity': affinity}

        # Create the user
        query = ("INSERT INTO user (id, email, password, affinity) "
                 "VALUES ('{}', '{}', '{}', '{}');").format(
            user_id, email, password, affinity
        )

        self.connect()
        try:
            self.cursor.execute(query)
        except MySQLdb.Error:
            self.conn.rollback()
            self.close()

            user_info.update({'error': 'Failed to create user.'})
            return user_info

        # If the user is staff, add the user-restaurant ID pair to the restaurant_staff table
        if affinity in ['staff', 'staff_only']:
            restaurant_staff_id = self.generate_id()
            query = ("INSERT INTO restaurant_staff (id, user_id, restaurant_id) "
                     "VALUES ('{}', '{}', '{}');").format(
                restaurant_staff_id, user_id, restaurant_id
            )
            try:
                self.cursor.execute(query)
            except MySQLdb.Error:
                self.conn.rollback()
                self.close()

                user_info.update({'error': 'Failed to link user to restaurant.'})
                return user_info
            user_info.update({'restaurant_id': restaurant_id})

        self.conn.commit()
        self.close()

        user_info.update({'id': user_id})
        return user_info

    def get_user_restaurant(self, user_id):
        """
        Retrieve the ID of the restaurant that the user works for.
        :param user_id:
        :return:
        """
        self.connect()

        restaurant_id = ''
        query = ("SELECT restaurant_id FROM restaurant_staff "
                 "WHERE user_id = '{}';").format(user_id)
        try:
            self.cursor.execute(query)
            restaurant_id = self.cursor.fetchone()['restaurant_id']
        except MySQLdb.Error:
            print "Error: Unable to fetch data."

        self.close()
        return restaurant_id

    def get_reg_id(self, user_id):
        self.connect()

        android_reg_id = ''
        query = ("SELECT android_reg_id FROM user "
                 "WHERE id = '{}';").format(user_id)
        try:
            self.cursor.execute(query)
            result = self.cursor.fetchone()
            if result is not None:
                android_reg_id = result['android_reg_id']
        except MySQLdb.Error:
            print "Error: Unable to fetch data."

        self.close()
        return android_reg_id

    def update_reg_id(self, email, reg_id):
        """
        Update a user's unique app registration id given their email.
        :param email:
        :param reg_id:
        :return:
        """
        success = False
        self.connect()

        query = "UPDATE user SET android_reg_id = '{}' WHERE email = '{}';".format(reg_id, email)
        try:
            self.cursor.execute(query)
            success = True
        except MySQLdb.Error:
            print "Error: Unable to update data."
            self.conn.rollback()
            self.close()
            return success

        self.conn.commit()
        self.close()
        return success

    def delete_user(self, email):
        self.connect()

        query = "DELETE FROM user WHERE email='{}';".format(email)

        try:
            self.cursor.execute(query)
        except MySQLdb.Error:
            print "Error: Unable to delete user."
            self.conn.rollback()
            self.close()
            return False

        self.conn.commit()
        self.close()
        return True

    def delete_from_staff(self, user_id):
        self.connect()

        query = "DELETE FROM restaurant_staff WHERE user_id='{}';".format(user_id)

        try:
            self.cursor.execute(query)
        except MySQLdb.Error:
            print "Error: Unable to delete user from staff table."
            self.conn.rollback()
            self.close()
            return False

        self.conn.commit()
        self.close()
        return True
