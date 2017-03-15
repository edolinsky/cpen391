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
        pass
