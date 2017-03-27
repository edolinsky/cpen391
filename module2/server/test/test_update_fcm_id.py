from __future__ import absolute_import
from flask_testing import LiveServerTestCase
import requests
import server
from user import User


class UpdateFCMIDTests(LiveServerTestCase):
    update_fcm_id_endpoint = "/update_fcm_id"

    def create_app(self):
        app = server.app
        app.config['TESTING'] = True

        # Set to 0 to have the OS pick the port.
        app.config['LIVESERVER_PORT'] = 0
        return app

    @classmethod
    def setUpClass(cls):
        pass

    @classmethod
    def tearDownClass(cls):
        pass

    def setUp(self):
        self.test_email = "test_user@dolins.ky"
        self.test_reg_id = "Attack of the Clones"
        self.test_user = User(self.test_email)
        self.request_body = {
            "user": self.test_email,
            "android_reg_id": self.test_reg_id
        }

    def post_update_fcm_id(self):
        return requests.post(self.get_server_url() + self.update_fcm_id_endpoint,
                             json=self.request_body)

    def test_post_success(self):
        r = self.post_update_fcm_id()

        self.assertEqual(r.status_code, server.CREATED)
        self.assertTrue('message' in r.json())

    def test_post_no_user(self):
        self.request_body.pop('user')
        r = self.post_update_fcm_id()

        self.assertEqual(r.status_code, server.BAD_REQUEST)
        self.assertTrue('error' in r.json())

    def test_post_user_nonexistent(self):
        self.request_body.update({'user': self.get_server_url()})
        r = self.post_update_fcm_id()

        self.assertEqual(r.status_code, server.UNAUTHORIZED)
        self.assertTrue('error' in r.json())

    def test_post_no_fcm_id(self):
        self.request_body.pop("android_reg_id")
        r1 = self.post_update_fcm_id()

        self.assertEqual(r1.status_code, server.BAD_REQUEST)
        user_id = self.test_user.get_id()

        self.request_body.update({"android_reg_id": self.test_reg_id})
        r2 = self.post_update_fcm_id()

        self.assertEqual(r2.status_code, server.CREATED)
        self.assertEqual(self.test_user.get_app_id(user_id=user_id), self.test_reg_id)
