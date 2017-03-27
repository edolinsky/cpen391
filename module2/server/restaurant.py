import db
from db.restaurant_db import RestaurantDb


class Restaurant:
    def __init__(self, restaurant_id):
        self.restaurant_id = restaurant_id
        self.db = RestaurantDb(user=db.db_user,
                               passwd=db.db_passwd,
                               host=db.db_host,
                               db=db.db_database,
                               port=db.db_port)

    def exists(self):
        """
        Determines whether this restaurant exists.
        :return: boolean
        """
        return self.db.restaurant_exists(self.restaurant_id)

    def get_open_orders(self):
        """
        Retrieves all open orders for this restaurant.
        :return:
        """
        return self.db.select_all_open_orders(self.restaurant_id)

    def create(self, name):
        """
        Creates a restaurant entity in the database.
        :param name: Name string of the restaurant.
        :return: Response JSON body.
        """
        self.restaurant_id = self.db.generate_id()

        return self.db.create_restaurant(restaurant_id=self.restaurant_id, name=name)

    def get_staff_hub_mappings(self):
        """
        Get all staff/table hub device mappings for this restaurant.
        :return: Response JSON body.
        """
        return self.db.get_staff_hub_mappings(restaurant_id=self.restaurant_id)

    def update_staff_hub_mappings(self, mapping_info):
        """
        Update a subset of staff/table hub device mappings for this restaurant.
        :param mapping_info: Dictionary containing a list of such mappings.
        :return: Response JSON body.
        """
        return self.db.update_staff_hub_mappings(mapping_info=mapping_info)

    def remove_staff_from_mappings(self, staff_id):
        """
        Remove the specified staff user from all mappings for this restaurant.
        :param staff_id: User ID string.
        :return: Response JSON body.
        """
        return self.db.remove_staff_from_mappings(user_id=staff_id)

