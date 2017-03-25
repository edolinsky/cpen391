import db
import os
from pyfcm import FCMNotification

from db.hub_db import HubDb

api_key = os.environ.get('fcm_api_key')


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

    def get_table_name(self, hub_id):
        return self.db.get_table_name(hub_id=hub_id)

    @staticmethod
    def trigger_notification(attendant_app_id, table_name):
        push_service = FCMNotification(api_key=api_key)

        message_title = "Resty Update"
        message_body = "{} is requesting your service!".format(table_name)

        message = {'message_title': message_title,
                   'message_body': message_body}

        result = push_service.notify_single_device(registration_id=attendant_app_id,
                                                   data_message=message)
        if result['success'] == 1:
            return True
        else:
            print "Notification error: {}".format(result)
            return False
