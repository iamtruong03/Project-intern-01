import org.apache.commons.math3.stat.regression.SimpleRegression;

public class other {
  public static void main(String[] args) {
    SimpleRegression regression = new SimpleRegression();

    // Dữ liệu: (ngày, số công việc hoàn thành)
    regression.addData(1, 3);
    regression.addData(2, 5);
    regression.addData(3, 7);
    regression.addData(4, 9);

    // Dự đoán số công việc hoàn thành vào ngày thứ 5
    System.out.println("Predicted tasks on day 5: " + regression.predict(5));
  }
}
