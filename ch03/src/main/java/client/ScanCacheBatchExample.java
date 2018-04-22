package client;

// cc ScanCacheBatchExample Example using caching and batch parameters for scans
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.metrics.ScanMetrics;

import util.HBaseHelper;

public class ScanCacheBatchExample {

  private static HTableInterface table = null;

  // vv ScanCacheBatchExample
  private static void scan(int caching, int batch, boolean small)
  throws IOException {
    int count = 0;
    Scan scan = new Scan();
    scan.setCaching(caching);  // co ScanCacheBatchExample-1-Set Set caching and batch parameters.
    scan.setBatch(batch);
    scan.setSmall(small);
//    scan.setScanMetricsEnabled(true);
    ResultScanner scanner = table.getScanner(scan);
    for (Result result : scanner) {
      count++; // co ScanCacheBatchExample-2-Count Count the number of Results available.
    }
    scanner.close();
//    ScanMetrics metrics = scan.getScanMetrics();
//    System.out.println("Caching: " + caching + ", Batch: " + batch +
//      ", Small: " + small + ", Results: " + count +
//      ", RPCs: " + metrics.countOfRPCcalls);
  }

  public static void main(String[] args) throws IOException {
    // ^^ ScanCacheBatchExample
    Configuration conf = HBaseConfiguration.create();

    HBaseHelper helper = HBaseHelper.getHelper(conf);
    helper.dropTable("testtable");
    helper.createTable("testtable", "colfam1", "colfam2");
    helper.fillTable("testtable", 1, 10, 10, "colfam1", "colfam2");

    HConnection connection = HConnectionManager.createConnection(conf);
    table = connection.getTable(TableName.valueOf("testtable"));

    // vv ScanCacheBatchExample
    /*...*/
    scan(1, 1, false);
    scan(1, 0, false);
    scan(1, 0, true);
    scan(200, 1, false);
    scan(200, 0, false);
    scan(200, 0, true);
    scan(2000, 100, false); // co ScanCacheBatchExample-3-Test Test various combinations.
    scan(2, 100, false);
    scan(2, 10, false);
    scan(5, 100, false);
    scan(5, 20, false);
    scan(10, 10, false);
    /*...*/
    // ^^ ScanCacheBatchExample
    table.close();
    connection.close();
    helper.close();
    // vv ScanCacheBatchExample
  }
  // ^^ ScanCacheBatchExample
}
