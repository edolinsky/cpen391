import db
from db.hub_db import HubDb


class Hub:
    def __init__(self, restaurant_id):
        self.restaurant_id = restaurant_id
        self.db = HubDb(user=db.db_user,
                        passwd=db.db_passwd,
                        host=db.db_host,
                        db=db.db_database,
                        port=db.db_port)

    def is_registered(self, hub_id):
        return self.db.hub_is_affiliated(hub_id=hub_id,
                                         restaurant_id=self.restaurant_id)

    def get_attendant_id(self, hub_id):
        return self.db.get_hub_attendant_id(hub_id=hub_id)

    def trigger_notification(self, attendant_id):
        pass
