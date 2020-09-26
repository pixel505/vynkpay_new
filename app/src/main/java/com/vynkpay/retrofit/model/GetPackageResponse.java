package com.vynkpay.retrofit.model;

import java.util.List;

public class GetPackageResponse {


    /**
     * status : true
     * data : {"package_buy":{"id":"99","user_id":"62","ref_id":"1451","package_id":"11","payment_via":"2","type":"1","title":"$100.00 (2 Points)","status":"1","active_date":"2020-07-03 15:18:48","booster":"0","level_income_distributed":"0","daily_percentage":"0","daily_amount":"0.00","weekly_percentage":"0","weekly_amount":"0.00","monthly_percentage":"0","binary_percentage":"0","description":"","pv":"0","capping":"0","price":"8000.00","tds":"0","gst":"1440","total_price":"9440.00","currency_value":"80","points":"2","bit_address":"","units":"0","power":"0","total_power":"0","duration":"52","typeofinvest":"0","pool_amount":"0","pool_status":"0","pool_update_date":"0000-00-00 00:00:00","setup":null,"created_date":"2020-07-03 15:18:48","modified_date":"2020-07-03 15:18:48","isactive":"1"},"earningTotalEarnedOnly":"25635.75","packages":[{"id":"12","type":"1","title":"$250.00 (5 Points)","price":"20000.00","tds":"0.00","gst":"3600","total_amount":"23600.00","currency_value":"80","points":"5","required_earning":"100000.00","required_earning_notification":"90000.00","cashback":"20000","lock_in_period":"1","daily_percentage":"0","weekly_percentage":"0","daily_amount":"0.00","weekly_amount":"0.00","ditribution":"15","monthly_percentage":"0","description":"$250.00 (5 Points) x Rs. 80 + GST (for India Nation) 23600/-\r\n\r\n\r\nAfter earnings of $1250 user must to buy $250 Package, notification on $1125","step_binary_pv":"0","binary_percentage":"0.00","capping":"0.00","gps_charges":"0.00","roadside_assistance":"0.00","daily_rent":"0.00","setup":"","duration":"52","img":null,"ditribution_b":"0","daily_amount_b":"0.00","binary_percentage_b":"0","pool_amount":"0.00","created_date":"2018-04-18 00:00:00","modified_date":"2020-06-06 17:36:27","isactive":"1","curser":"allowed"}]}
     * message : purchase data list
     */

    private String status;
    private DataBean data;
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean {
        /**
         * package_buy : {"id":"99","user_id":"62","ref_id":"1451","package_id":"11","payment_via":"2","type":"1","title":"$100.00 (2 Points)","status":"1","active_date":"2020-07-03 15:18:48","booster":"0","level_income_distributed":"0","daily_percentage":"0","daily_amount":"0.00","weekly_percentage":"0","weekly_amount":"0.00","monthly_percentage":"0","binary_percentage":"0","description":"","pv":"0","capping":"0","price":"8000.00","tds":"0","gst":"1440","total_price":"9440.00","currency_value":"80","points":"2","bit_address":"","units":"0","power":"0","total_power":"0","duration":"52","typeofinvest":"0","pool_amount":"0","pool_status":"0","pool_update_date":"0000-00-00 00:00:00","setup":null,"created_date":"2020-07-03 15:18:48","modified_date":"2020-07-03 15:18:48","isactive":"1"}
         * earningTotalEarnedOnly : 25635.75
         * packages : [{"id":"12","type":"1","title":"$250.00 (5 Points)","price":"20000.00","tds":"0.00","gst":"3600","total_amount":"23600.00","currency_value":"80","points":"5","required_earning":"100000.00","required_earning_notification":"90000.00","cashback":"20000","lock_in_period":"1","daily_percentage":"0","weekly_percentage":"0","daily_amount":"0.00","weekly_amount":"0.00","ditribution":"15","monthly_percentage":"0","description":"$250.00 (5 Points) x Rs. 80 + GST (for India Nation) 23600/-\r\n\r\n\r\nAfter earnings of $1250 user must to buy $250 Package, notification on $1125","step_binary_pv":"0","binary_percentage":"0.00","capping":"0.00","gps_charges":"0.00","roadside_assistance":"0.00","daily_rent":"0.00","setup":"","duration":"52","img":null,"ditribution_b":"0","daily_amount_b":"0.00","binary_percentage_b":"0","pool_amount":"0.00","created_date":"2018-04-18 00:00:00","modified_date":"2020-06-06 17:36:27","isactive":"1","curser":"allowed"}]
         */

        private PackageBuyBean package_buy;
        private String earningTotalEarnedOnly;
        private List<PackagesBean> packages;

        public PackageBuyBean getPackage_buy() {
            return package_buy;
        }

        public void setPackage_buy(PackageBuyBean package_buy) {
            this.package_buy = package_buy;
        }

        public String getEarningTotalEarnedOnly() {
            return earningTotalEarnedOnly;
        }

        public void setEarningTotalEarnedOnly(String earningTotalEarnedOnly) {
            this.earningTotalEarnedOnly = earningTotalEarnedOnly;
        }

        public List<PackagesBean> getPackages() {
            return packages;
        }

        public void setPackages(List<PackagesBean> packages) {
            this.packages = packages;
        }

        public static class PackageBuyBean {
            /**
             * id : 99
             * user_id : 62
             * ref_id : 1451
             * package_id : 11
             * payment_via : 2
             * type : 1
             * title : $100.00 (2 Points)
             * status : 1
             * active_date : 2020-07-03 15:18:48
             * booster : 0
             * level_income_distributed : 0
             * daily_percentage : 0
             * daily_amount : 0.00
             * weekly_percentage : 0
             * weekly_amount : 0.00
             * monthly_percentage : 0
             * binary_percentage : 0
             * description :
             * pv : 0
             * capping : 0
             * price : 8000.00
             * tds : 0
             * gst : 1440
             * total_price : 9440.00
             * currency_value : 80
             * points : 2
             * bit_address :
             * units : 0
             * power : 0
             * total_power : 0
             * duration : 52
             * typeofinvest : 0
             * pool_amount : 0
             * pool_status : 0
             * pool_update_date : 0000-00-00 00:00:00
             * setup : null
             * created_date : 2020-07-03 15:18:48
             * modified_date : 2020-07-03 15:18:48
             * isactive : 1
             */

            private String id;
            private String user_id;
            private String ref_id;
            private String package_id;
            private String payment_via;
            private String type;
            private String title;
            private String status;
            private String active_date;
            private String booster;
            private String level_income_distributed;
            private String daily_percentage;
            private String daily_amount;
            private String weekly_percentage;
            private String weekly_amount;
            private String monthly_percentage;
            private String binary_percentage;
            private String description;
            private String pv;
            private String capping;
            private String price;
            private String tds;
            private String gst;
            private String total_price;
            private String currency_value;
            private String points;
            private String bit_address;
            private String units;
            private String power;
            private String total_power;
            private String duration;
            private String typeofinvest;
            private String pool_amount;
            private String pool_status;
            private String pool_update_date;
            private Object setup;
            private String created_date;
            private String modified_date;
            private String isactive;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public String getRef_id() {
                return ref_id;
            }

            public void setRef_id(String ref_id) {
                this.ref_id = ref_id;
            }

            public String getPackage_id() {
                return package_id;
            }

            public void setPackage_id(String package_id) {
                this.package_id = package_id;
            }

            public String getPayment_via() {
                return payment_via;
            }

            public void setPayment_via(String payment_via) {
                this.payment_via = payment_via;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getActive_date() {
                return active_date;
            }

            public void setActive_date(String active_date) {
                this.active_date = active_date;
            }

            public String getBooster() {
                return booster;
            }

            public void setBooster(String booster) {
                this.booster = booster;
            }

            public String getLevel_income_distributed() {
                return level_income_distributed;
            }

            public void setLevel_income_distributed(String level_income_distributed) {
                this.level_income_distributed = level_income_distributed;
            }

            public String getDaily_percentage() {
                return daily_percentage;
            }

            public void setDaily_percentage(String daily_percentage) {
                this.daily_percentage = daily_percentage;
            }

            public String getDaily_amount() {
                return daily_amount;
            }

            public void setDaily_amount(String daily_amount) {
                this.daily_amount = daily_amount;
            }

            public String getWeekly_percentage() {
                return weekly_percentage;
            }

            public void setWeekly_percentage(String weekly_percentage) {
                this.weekly_percentage = weekly_percentage;
            }

            public String getWeekly_amount() {
                return weekly_amount;
            }

            public void setWeekly_amount(String weekly_amount) {
                this.weekly_amount = weekly_amount;
            }

            public String getMonthly_percentage() {
                return monthly_percentage;
            }

            public void setMonthly_percentage(String monthly_percentage) {
                this.monthly_percentage = monthly_percentage;
            }

            public String getBinary_percentage() {
                return binary_percentage;
            }

            public void setBinary_percentage(String binary_percentage) {
                this.binary_percentage = binary_percentage;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getPv() {
                return pv;
            }

            public void setPv(String pv) {
                this.pv = pv;
            }

            public String getCapping() {
                return capping;
            }

            public void setCapping(String capping) {
                this.capping = capping;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getTds() {
                return tds;
            }

            public void setTds(String tds) {
                this.tds = tds;
            }

            public String getGst() {
                return gst;
            }

            public void setGst(String gst) {
                this.gst = gst;
            }

            public String getTotal_price() {
                return total_price;
            }

            public void setTotal_price(String total_price) {
                this.total_price = total_price;
            }

            public String getCurrency_value() {
                return currency_value;
            }

            public void setCurrency_value(String currency_value) {
                this.currency_value = currency_value;
            }

            public String getPoints() {
                return points;
            }

            public void setPoints(String points) {
                this.points = points;
            }

            public String getBit_address() {
                return bit_address;
            }

            public void setBit_address(String bit_address) {
                this.bit_address = bit_address;
            }

            public String getUnits() {
                return units;
            }

            public void setUnits(String units) {
                this.units = units;
            }

            public String getPower() {
                return power;
            }

            public void setPower(String power) {
                this.power = power;
            }

            public String getTotal_power() {
                return total_power;
            }

            public void setTotal_power(String total_power) {
                this.total_power = total_power;
            }

            public String getDuration() {
                return duration;
            }

            public void setDuration(String duration) {
                this.duration = duration;
            }

            public String getTypeofinvest() {
                return typeofinvest;
            }

            public void setTypeofinvest(String typeofinvest) {
                this.typeofinvest = typeofinvest;
            }

            public String getPool_amount() {
                return pool_amount;
            }

            public void setPool_amount(String pool_amount) {
                this.pool_amount = pool_amount;
            }

            public String getPool_status() {
                return pool_status;
            }

            public void setPool_status(String pool_status) {
                this.pool_status = pool_status;
            }

            public String getPool_update_date() {
                return pool_update_date;
            }

            public void setPool_update_date(String pool_update_date) {
                this.pool_update_date = pool_update_date;
            }

            public Object getSetup() {
                return setup;
            }

            public void setSetup(Object setup) {
                this.setup = setup;
            }

            public String getCreated_date() {
                return created_date;
            }

            public void setCreated_date(String created_date) {
                this.created_date = created_date;
            }

            public String getModified_date() {
                return modified_date;
            }

            public void setModified_date(String modified_date) {
                this.modified_date = modified_date;
            }

            public String getIsactive() {
                return isactive;
            }

            public void setIsactive(String isactive) {
                this.isactive = isactive;
            }
        }

        public static class PackagesBean {
            /**
             * id : 12
             * type : 1
             * title : $250.00 (5 Points)
             * price : 20000.00
             * tds : 0.00
             * gst : 3600
             * total_amount : 23600.00
             * currency_value : 80
             * points : 5
             * required_earning : 100000.00
             * required_earning_notification : 90000.00
             * cashback : 20000
             * lock_in_period : 1
             * daily_percentage : 0
             * weekly_percentage : 0
             * daily_amount : 0.00
             * weekly_amount : 0.00
             * ditribution : 15
             * monthly_percentage : 0
             * description : $250.00 (5 Points) x Rs. 80 + GST (for India Nation) 23600/-


             After earnings of $1250 user must to buy $250 Package, notification on $1125
             * step_binary_pv : 0
             * binary_percentage : 0.00
             * capping : 0.00
             * gps_charges : 0.00
             * roadside_assistance : 0.00
             * daily_rent : 0.00
             * setup :
             * duration : 52
             * img : null
             * ditribution_b : 0
             * daily_amount_b : 0.00
             * binary_percentage_b : 0
             * pool_amount : 0.00
             * created_date : 2018-04-18 00:00:00
             * modified_date : 2020-06-06 17:36:27
             * isactive : 1
             * curser : allowed
             */

            private String id;
            private String type;
            private String title;
            private String price;
            private String tds;
            private String gst;
            private String total_amount;
            private String currency_value;
            private String points;
            private String required_earning;
            private String required_earning_notification;
            private String cashback;
            private String lock_in_period;
            private String daily_percentage;
            private String weekly_percentage;
            private String daily_amount;
            private String weekly_amount;
            private String ditribution;
            private String monthly_percentage;
            private String description;
            private String step_binary_pv;
            private String binary_percentage;
            private String capping;
            private String gps_charges;
            private String roadside_assistance;
            private String daily_rent;
            private String setup;
            private String duration;
            private Object img;
            private String ditribution_b;
            private String daily_amount_b;
            private String binary_percentage_b;
            private String pool_amount;
            private String created_date;
            private String modified_date;
            private String isactive;
            private String curser;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getTds() {
                return tds;
            }

            public void setTds(String tds) {
                this.tds = tds;
            }

            public String getGst() {
                return gst;
            }

            public void setGst(String gst) {
                this.gst = gst;
            }

            public String getTotal_amount() {
                return total_amount;
            }

            public void setTotal_amount(String total_amount) {
                this.total_amount = total_amount;
            }

            public String getCurrency_value() {
                return currency_value;
            }

            public void setCurrency_value(String currency_value) {
                this.currency_value = currency_value;
            }

            public String getPoints() {
                return points;
            }

            public void setPoints(String points) {
                this.points = points;
            }

            public String getRequired_earning() {
                return required_earning;
            }

            public void setRequired_earning(String required_earning) {
                this.required_earning = required_earning;
            }

            public String getRequired_earning_notification() {
                return required_earning_notification;
            }

            public void setRequired_earning_notification(String required_earning_notification) {
                this.required_earning_notification = required_earning_notification;
            }

            public String getCashback() {
                return cashback;
            }

            public void setCashback(String cashback) {
                this.cashback = cashback;
            }

            public String getLock_in_period() {
                return lock_in_period;
            }

            public void setLock_in_period(String lock_in_period) {
                this.lock_in_period = lock_in_period;
            }

            public String getDaily_percentage() {
                return daily_percentage;
            }

            public void setDaily_percentage(String daily_percentage) {
                this.daily_percentage = daily_percentage;
            }

            public String getWeekly_percentage() {
                return weekly_percentage;
            }

            public void setWeekly_percentage(String weekly_percentage) {
                this.weekly_percentage = weekly_percentage;
            }

            public String getDaily_amount() {
                return daily_amount;
            }

            public void setDaily_amount(String daily_amount) {
                this.daily_amount = daily_amount;
            }

            public String getWeekly_amount() {
                return weekly_amount;
            }

            public void setWeekly_amount(String weekly_amount) {
                this.weekly_amount = weekly_amount;
            }

            public String getDitribution() {
                return ditribution;
            }

            public void setDitribution(String ditribution) {
                this.ditribution = ditribution;
            }

            public String getMonthly_percentage() {
                return monthly_percentage;
            }

            public void setMonthly_percentage(String monthly_percentage) {
                this.monthly_percentage = monthly_percentage;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getStep_binary_pv() {
                return step_binary_pv;
            }

            public void setStep_binary_pv(String step_binary_pv) {
                this.step_binary_pv = step_binary_pv;
            }

            public String getBinary_percentage() {
                return binary_percentage;
            }

            public void setBinary_percentage(String binary_percentage) {
                this.binary_percentage = binary_percentage;
            }

            public String getCapping() {
                return capping;
            }

            public void setCapping(String capping) {
                this.capping = capping;
            }

            public String getGps_charges() {
                return gps_charges;
            }

            public void setGps_charges(String gps_charges) {
                this.gps_charges = gps_charges;
            }

            public String getRoadside_assistance() {
                return roadside_assistance;
            }

            public void setRoadside_assistance(String roadside_assistance) {
                this.roadside_assistance = roadside_assistance;
            }

            public String getDaily_rent() {
                return daily_rent;
            }

            public void setDaily_rent(String daily_rent) {
                this.daily_rent = daily_rent;
            }

            public String getSetup() {
                return setup;
            }

            public void setSetup(String setup) {
                this.setup = setup;
            }

            public String getDuration() {
                return duration;
            }

            public void setDuration(String duration) {
                this.duration = duration;
            }

            public Object getImg() {
                return img;
            }

            public void setImg(Object img) {
                this.img = img;
            }

            public String getDitribution_b() {
                return ditribution_b;
            }

            public void setDitribution_b(String ditribution_b) {
                this.ditribution_b = ditribution_b;
            }

            public String getDaily_amount_b() {
                return daily_amount_b;
            }

            public void setDaily_amount_b(String daily_amount_b) {
                this.daily_amount_b = daily_amount_b;
            }

            public String getBinary_percentage_b() {
                return binary_percentage_b;
            }

            public void setBinary_percentage_b(String binary_percentage_b) {
                this.binary_percentage_b = binary_percentage_b;
            }

            public String getPool_amount() {
                return pool_amount;
            }

            public void setPool_amount(String pool_amount) {
                this.pool_amount = pool_amount;
            }

            public String getCreated_date() {
                return created_date;
            }

            public void setCreated_date(String created_date) {
                this.created_date = created_date;
            }

            public String getModified_date() {
                return modified_date;
            }

            public void setModified_date(String modified_date) {
                this.modified_date = modified_date;
            }

            public String getIsactive() {
                return isactive;
            }

            public void setIsactive(String isactive) {
                this.isactive = isactive;
            }

            public String getCurser() {
                return curser;
            }

            public void setCurser(String curser) {
                this.curser = curser;
            }
        }
    }
}
