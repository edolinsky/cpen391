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
        return self.db.restaurant_exists(self.restaurant_id)

    def get_open_orders(self):
        return self.db.select_all_open_orders(self.restaurant_id)

    def create(self, name):
        self.restaurant_id = self.db.generate_id()

        return self.db.create_restaurant(restaurant_id=self.restaurant_id, name=name)

