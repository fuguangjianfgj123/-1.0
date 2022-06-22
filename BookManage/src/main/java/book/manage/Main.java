package book.manage;


import book.manage.entity.Book;
import book.manage.entity.Student;
import book.manage.sql.SqlUtil;
import lombok.extern.java.Log;
import org.apache.ibatis.io.Resources;


import java.util.Scanner;
import java.util.logging.LogManager;

@Log
public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        LogManager manager = LogManager.getLogManager();
        manager.readConfiguration(Resources.getResourceAsStream("logging.properties"));
        while (true) {
            System.out.println("==========录入===========");
            System.out.println("1. 录入学生信息");
            System.out.println("2. 录入书籍信息");
            System.out.println("3. 添加借阅信息");
            System.out.println("===========查询==========");
            System.out.println("4. 查询学生信息");
            System.out.println("5. 查询书籍信息");
            System.out.println("6. 查询借阅信息");
            System.out.print("输入操作：按其他任意数字退出  ");
            int input;
            try {
                input = scanner.nextInt();

            } catch (Exception e) {
                return;
            }
            scanner.nextLine();
            switch (input) {
                case 1:
                    addStudent(scanner);
                    break;
                case 2:
                    addBook(scanner);
                    break;
                case 3:
                    addBorrow(scanner);
                    break;
                case 4:
                    showStudent();
                    break;
                case 5:
                    showBook();
                    break;
                case 6:
                    showBorrow();
                    break;
                default:
                    return;
            }

        }
    }
    private static void showStudent(){
        SqlUtil.doSqlWork(mapper -> {
            mapper.getStudentList().forEach(student -> {
                System.out.println(student.getSid() + "." + student.getName() + " " + student.getSex() + " " + student.getGrade());
            });
        });
    }
    private static void showBook(){
        SqlUtil.doSqlWork(mapper ->{
            mapper.getBookList().forEach(book -> {
                System.out.println(book.getBid() + "." + book.getTitle() + " " + book.getPrice() );

            });
        });
    }

    private static void showBorrow() {
        SqlUtil.doSqlWork(mapper -> {
            mapper.getBorrowList().forEach(borrow -> {
                System.out.println(borrow.getStudent().getName() + " -> " + borrow.getBook().getTitle());
            });
        });
    }

    private static void addStudent(Scanner scanner) {
        System.out.print("请输入学生名字：");
        String name = scanner.nextLine();
        System.out.print("请输入学生性别（男/女）：");
        String sex = scanner.nextLine();
        System.out.print("请输入学生年级：");
        String grade = scanner.nextLine();
        int g = Integer.parseInt(grade);
        Student student = new Student(name, sex, g);
        SqlUtil.doSqlWork(mapper -> {
            int i = mapper.addStudent(student);
            if (i > 0) {
                System.out.println("学生信息录入成功");
                log.info("插入新添加一条学生信息" + student);
            } else System.out.println("学生信息录入失败，请重试");
        });
    }

    private static void addBook(Scanner scanner) {
        System.out.print("请输入书籍标题：");
        String title = scanner.nextLine();
        System.out.print("请输入书籍介绍：");
        String desc = scanner.nextLine();
        System.out.print("请输入书籍价格：");
        String price = scanner.nextLine();
        double p = Double.parseDouble(price);
        Book book = new Book(title, desc, p);
        SqlUtil.doSqlWork(mapper -> {
            int i = mapper.addBook(book);
            if (i > 0) {
                System.out.println("书籍信息录入成功");
                log.info("插入新添加一条书籍信息" + book);
            } else System.out.println("书籍信息录入失败，请重试");
        });
    }

    private static void addBorrow(Scanner scanner) {
        System.out.println("请输入书籍号：");
        String a = scanner.nextLine();
        int bid = Integer.parseInt(a);
        System.out.println("请输入学号：");
        String b = scanner.nextLine();
        int sid = Integer.parseInt(b);
        SqlUtil.doSqlWork(mapper -> mapper.addBorrow(sid, bid));
    }
}
