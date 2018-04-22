===========================
HBase: The Definitive Guide
===========================

This repository has all the code as used in the HBase book.

==replace:
org.apache.hadoop.hbase.client.HConnectionFactory

org.apache.hadoop.hbase.client.HConnectionManager

org.apache.hadoop.hbase.client.Table

org.apache.hadoop.hbase.client.HTableInterface

Connection connection = ConnectionFactory.createConnection(conf);

HConnection  connection = HConnectionManager.createConnection(configuration);


HTableInterface tbl = connection.getTable

delete.addColumns
delete.deleteColumns

Admin admin = connection.getAdmin();
HBaseAdmin admin = new HBaseAdmin(connection);

org.apache.hadoop.hbase.client.Admin
org.apache.hadoop.hbase.client.HBaseAdmin