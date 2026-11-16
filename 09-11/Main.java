import java.util.ArrayList;
import java.util.List;
import java.util.Random;

interface Shape {
    double area();
    double perimeter();
    double distance(Shape other);
    void move(double dx, double dy);
    void rotate(double degrees);
    void zoom(double ratio);
    Point getCenter();
}

abstract class AbstractShape implements Shape {
    public double distance(Shape other) {
        return getCenter().dist(other.getCenter());
    }
}

class Point extends AbstractShape {
    private double x;
    private double y;

    public Point() {
        this(0, 0);
    }

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double dist(Point p) {
        double dx = x - p.x;
        double dy = y - p.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public void rotateAround(Point c, double degrees) {
        double rad = Math.toRadians(degrees);
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);
        double dx = x - c.x;
        double dy = y - c.y;
        x = c.x + dx * cos - dy * sin;
        y = c.y + dx * sin + dy * cos;
    }

    public void scaleAround(Point c, double ratio) {
        x = c.x + (x - c.x) * ratio;
        y = c.y + (y - c.y) * ratio;
    }

    public double area() {
        return 0;
    }

    public double perimeter() {
        return 0;
    }

    public void move(double dx, double dy) {
        x += dx;
        y += dy;
    }

    public void rotate(double degrees) {
    }

    public void zoom(double ratio) {
    }

    public Point getCenter() {
        return this;
    }

    public String toString() {
        return String.format("Point(%.2f, %.2f)", x, y);
    }
}

class Circle extends AbstractShape {
    private Point center;
    private double radius;

    public Circle(Point center, double radius) {
        this.center = center;
        this.radius = radius;
    }

    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center) {
        this.center = center;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double area() {
        return Math.PI * radius * radius;
    }

    public double perimeter() {
        return 2 * Math.PI * radius;
    }

    public void move(double dx, double dy) {
        center.move(dx, dy);
    }

    public void rotate(double degrees) {
    }

    public void zoom(double ratio) {
        radius *= ratio;
    }

    public String toString() {
        return String.format("Circle(center=%s, radius=%.2f)", center, radius);
    }
}

class Line extends AbstractShape {
    private Point p1;
    private Point p2;

    public Line(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public Point getP1() {
        return p1;
    }

    public void setP1(Point p1) {
        this.p1 = p1;
    }

    public Point getP2() {
        return p2;
    }

    public void setP2(Point p2) {
        this.p2 = p2;
    }

    public double length() {
        return p1.dist(p2);
    }

    public double area() {
        return 0;
    }

    public double perimeter() {
        return length();
    }

    public Point getCenter() {
        return new Point((p1.getX() + p2.getX()) / 2, (p1.getY() + p2.getY()) / 2);
    }

    public void move(double dx, double dy) {
        p1.move(dx, dy);
        p2.move(dx, dy);
    }

    public void rotate(double degrees) {
        Point c = getCenter();
        p1.rotateAround(c, degrees);
        p2.rotateAround(c, degrees);
    }

    public void zoom(double ratio) {
        Point c = getCenter();
        p1.scaleAround(c, ratio);
        p2.scaleAround(c, ratio);
    }

    public String toString() {
        return String.format("Line(%s -> %s, length=%.2f)", p1, p2, length());
    }
}

class Triangle extends AbstractShape {
    private Point a;
    private Point b;
    private Point c;

    public Triangle(Point a, Point b, Point c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public Point getA() {
        return a;
    }

    public void setA(Point a) {
        this.a = a;
    }

    public Point getB() {
        return b;
    }

    public void setB(Point b) {
        this.b = b;
    }

    public Point getC() {
        return c;
    }

    public void setC(Point c) {
        this.c = c;
    }

    public double area() {
        return Math.abs((b.getX() - a.getX()) * (c.getY() - a.getY())
                - (c.getX() - a.getX()) * (b.getY() - a.getY())) / 2;
    }

    public double perimeter() {
        return a.dist(b) + b.dist(c) + c.dist(a);
    }

    public Point getCenter() {
        return new Point((a.getX() + b.getX() + c.getX()) / 3,
                (a.getY() + b.getY() + c.getY()) / 3);
    }

    public void move(double dx, double dy) {
        a.move(dx, dy);
        b.move(dx, dy);
        c.move(dx, dy);
    }

    public void rotate(double degrees) {
        Point ct = getCenter();
        a.rotateAround(ct, degrees);
        b.rotateAround(ct, degrees);
        c.rotateAround(ct, degrees);
    }

    public void zoom(double ratio) {
        Point ct = getCenter();
        a.scaleAround(ct, ratio);
        b.scaleAround(ct, ratio);
        c.scaleAround(ct, ratio);
    }

    public String toString() {
        return String.format("Triangle(%s, %s, %s)", a, b, c);
    }
}

class Rectangle extends AbstractShape {
    private Point[] v;

    public Rectangle(Point topLeft, double width, double height) {
        double x = topLeft.getX();
        double y = topLeft.getY();
        v = new Point[] {
            new Point(x, y),
            new Point(x + width, y),
            new Point(x + width, y + height),
            new Point(x, y + height)
        };
    }

    public Point[] getVertices() {
        return v;
    }

    public double getWidth() {
        return v[0].dist(v[1]);
    }

    public double getHeight() {
        return v[1].dist(v[2]);
    }

    public double area() {
        return getWidth() * getHeight();
    }

    public double perimeter() {
        return 2 * (getWidth() + getHeight());
    }

    public Point getCenter() {
        return new Point((v[0].getX() + v[2].getX()) / 2, (v[0].getY() + v[2].getY()) / 2);
    }

    public void move(double dx, double dy) {
        for (Point p : v) {
            p.move(dx, dy);
        }
    }

    public void rotate(double degrees) {
        Point c = getCenter();
        for (Point p : v) {
            p.rotateAround(c, degrees);
        }
    }

    public void zoom(double ratio) {
        Point c = getCenter();
        for (Point p : v) {
            p.scaleAround(c, ratio);
        }
    }

    public String toString() {
        return String.format("Rectangle(width=%.2f, height=%.2f, center=%s)",
                getWidth(), getHeight(), getCenter());
    }
}

public class Main {
    private static final Random RND = new Random();

    private static double rand(double min, double max) {
        return min + RND.nextDouble() * (max - min);
    }

    private static Point randomPoint() {
        return new Point(rand(-50, 50), rand(-50, 50));
    }

    private static Shape randomShape() {
        switch (RND.nextInt(5)) {
            case 0:
                return randomPoint();
            case 1:
                return new Circle(randomPoint(), rand(1, 20));
            case 2:
                return new Line(randomPoint(), randomPoint());
            case 3:
                Triangle t;
                do {
                    t = new Triangle(randomPoint(), randomPoint(), randomPoint());
                } while (t.area() < 1);
                return t;
            default:
                return new Rectangle(randomPoint(), rand(1, 30), rand(1, 30));
        }
    }

    private static void display(List<Shape> shapes) {
        for (int i = 0; i < shapes.size(); i++) {
            Shape s = shapes.get(i);
            System.out.printf("%2d. %-70s area=%10.2f perimeter=%10.2f%n",
                    i + 1, s, s.area(), s.perimeter());
        }
    }

    private static double totalArea(List<Shape> shapes) {
        double sum = 0;
        for (Shape s : shapes) {
            sum += s.area();
        }
        return sum;
    }

    private static double totalPerimeter(List<Shape> shapes) {
        double sum = 0;
        for (Shape s : shapes) {
            sum += s.perimeter();
        }
        return sum;
    }

    private static Shape maxArea(List<Shape> shapes) {
        Shape best = shapes.get(0);
        for (Shape s : shapes) {
            if (s.area() > best.area()) {
                best = s;
            }
        }
        return best;
    }

    private static Shape minArea(List<Shape> shapes) {
        Shape best = shapes.get(0);
        for (Shape s : shapes) {
            if (s.area() < best.area()) {
                best = s;
            }
        }
        return best;
    }

    public static void main(String[] args) {
        List<Shape> shapes = new ArrayList<>();
        int n = 10;
        for (int i = 0; i < n; i++) {
            shapes.add(randomShape());
        }

        System.out.println("===== DANH SACH CAC HINH =====");
        display(shapes);

        System.out.println();
        System.out.printf("Tong chu vi : %.2f%n", totalPerimeter(shapes));
        System.out.printf("Tong dien tich: %.2f%n", totalArea(shapes));
        System.out.println("Hinh co dien tich lon nhat: " + maxArea(shapes));
        System.out.println("Hinh co dien tich nho nhat: " + minArea(shapes));

        System.out.println();
        System.out.printf("Khoang cach giua hinh 1 va hinh 2: %.2f%n",
                shapes.get(0).distance(shapes.get(1)));

        double ratio = 2.0;
        System.out.println();
        System.out.println("===== SAU KHI PHONG TO VOI RATIO = " + ratio + " =====");
        for (Shape s : shapes) {
            s.zoom(ratio);
        }
        display(shapes);

        System.out.println();
        System.out.println("===== SAU KHI DICH CHUYEN (5, -3) VA XOAY 45 DO =====");
        for (Shape s : shapes) {
            s.move(5, -3);
            s.rotate(45);
        }
        display(shapes);

        System.out.println();
        System.out.printf("Tong chu vi : %.2f%n", totalPerimeter(shapes));
        System.out.printf("Tong dien tich: %.2f%n", totalArea(shapes));
        System.out.println("Hinh co dien tich lon nhat: " + maxArea(shapes));
        System.out.println("Hinh co dien tich nho nhat: " + minArea(shapes));
    }
}