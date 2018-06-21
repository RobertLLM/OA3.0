package cn.invonate.ygoa3.Entry;

import java.util.List;

public class MailAddress {
    private List<AddressBean> address;

    public MailAddress(List<AddressBean> address) {
        this.address = address;
    }

    public List<AddressBean> getAddress() {
        return address;
    }

    public void setAddress(List<AddressBean> address) {
        this.address = address;
    }

    public static class AddressBean {
        private String userName;
        private String address;

        public AddressBean(String userName, String address) {
            this.userName = userName;
            this.address = address;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}
