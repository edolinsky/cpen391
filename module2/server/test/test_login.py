from __future__ import absolute_import
from flask_testing import LiveServerTestCase
import requests
import server
from user import User


class LoginTests(LiveServerTestCase):
    login_endpoint = "/login"

    def create_app(self):
        app = server.app
        app.config['TESTING'] = True

        # Set to 0 to have the OS pick the port.
        app.config['LIVESERVER_PORT'] = 0
        return app

    @classmethod
    def setUpClass(cls):
        cls.test_user = User(email='test_user@dolins.ky')

    @classmethod
    def tearDownClass(cls):
        pass

    def setUp(self):
        self.request_body = {"user": self.test_user.email, "password": "password"}

    def login_post(self):
        return requests.post(url=self.get_server_url() + self.login_endpoint,
                             json=self.request_body)

    def test_login_success(self):
        r = self.login_post()

        self.assertEqual(r.status_code, server.OK)
        self.assertTrue('id' in r.json())

    def test_staff_login(self):
        self.request_body.update({"user": "test_staff@dolins.ky"})
        r = self.login_post()

        self.assertEqual(r.status_code, server.OK)
        self.assertTrue('id' in r.json())
        self.assertTrue('restaurant_id' in r.json())

    def test_login_wrong_password(self):
        self.request_body.update({"password": "wrong_password"})

        r = self.login_post()
        self.assertEqual(r.status_code, server.UNAUTHORIZED)
        self.assertTrue('error' in r.json())

    def test_login_nonexistent(self):
        self.request_body.update({"user": "abcdef@jklmnop.wxyz",
                                  "password": "wrong_password"})
        r = self.login_post()

        self.assertEqual(r.status_code, server.UNAUTHORIZED)
        self.assertTrue('error' in r.json())

    def test_login_no_password(self):
        self.request_body.pop("user")
        r = self.login_post()

        self.assertEqual(r.status_code, server.BAD_REQUEST)
        self.assertTrue('error' in r.json())

    def test_login_no_user(self):
        self.request_body.pop("password")
        r = self.login_post()

        self.assertEqual(r.status_code, server.BAD_REQUEST)
        self.assertTrue('error' in r.json())

    def test_login_android_reg_update(self):
        reg_one = "A New Hope"
        reg_two = "Empire Strikes Back"
        self.request_body.update({"android_reg_id": reg_one})
        r1 = self.login_post()

        self.assertEqual(r1.status_code, server.OK)
        user_id = self.test_user.get_id()
        self.assertEqual(self.test_user.get_app_id(user_id=user_id), reg_one)

        self.request_body.update({"android_reg_id": reg_two})
        r1 = self.login_post()

        self.assertEqual(r1.status_code, server.OK)
        self.assertEqual(self.test_user.get_app_id(user_id=user_id), reg_two)
