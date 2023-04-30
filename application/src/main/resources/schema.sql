--drop table replay_message;
CREATE TABLE IF NOT EXISTS replay_message (
  paymentid text NOT NULL,
  message text NOT NULL,
  createdTime timestamp  NOT NULL,
  expireTime timestamp  NOT NULL,
  country varchar(2),
  replay integer NOT NULL DEFAULT '1',
  status integer NOT NULL DEFAULT '0',
  PRIMARY KEY (paymentid)
);