-- Create the database
CREATE DATABASE BlindBox;
GO

USE BlindBox;
GO

-- Create Users table
CREATE TABLE Users (
    userID VARCHAR(50) PRIMARY KEY,
    password VARCHAR(50) NOT NULL,
    fullName NVARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20) NOT NULL,
    address NVARCHAR(255) NOT NULL,
    role VARCHAR(10) NOT NULL CHECK (role IN ('ADMIN', 'USER')),
    status BIT NOT NULL DEFAULT 1
);
GO

-- Create Categories table
CREATE TABLE Categories (
    categoryID INT PRIMARY KEY IDENTITY(1,1),
    categoryName NVARCHAR(100) NOT NULL,
    description NVARCHAR(500) NULL
);
GO

-- Create Products table (equivalent to Books in the original schema)
CREATE TABLE Products (
    productID INT PRIMARY KEY IDENTITY(1,1),
    productName NVARCHAR(255) NOT NULL,
    series NVARCHAR(100) NOT NULL,
    description NVARCHAR(1000) NULL,
    price DECIMAL(10, 2) NOT NULL,
    quantity INT NOT NULL DEFAULT 0,
    imageUrl VARCHAR(255) NOT NULL,
    categoryID INT FOREIGN KEY REFERENCES Categories(categoryID),
    createDate DATETIME NOT NULL DEFAULT GETDATE(),
    lastUpdateDate DATETIME NULL,
    lastUpdateUser VARCHAR(50) NULL,
    status BIT NOT NULL DEFAULT 1
);
GO

-- Create Orders table
CREATE TABLE Orders (
    orderID INT PRIMARY KEY IDENTITY(1,1),
    userID VARCHAR(50) NULL FOREIGN KEY REFERENCES Users(userID),
    orderDate DATETIME NOT NULL DEFAULT GETDATE(),
    totalAmount DECIMAL(10, 2) NOT NULL,
    customerName NVARCHAR(100) NOT NULL,
    customerEmail VARCHAR(100) NOT NULL,
    customerPhone VARCHAR(20) NOT NULL,
    customerAddress NVARCHAR(255) NOT NULL,
    paymentMethod VARCHAR(20) NOT NULL CHECK (paymentMethod IN ('PAYPAL', 'CASH', 'CREDIT_CARD')),
    paymentStatus VARCHAR(20) NOT NULL CHECK (paymentStatus IN ('PENDING', 'COMPLETED', 'FAILED', 'REFUNDED')),
    orderStatus VARCHAR(20) NOT NULL DEFAULT 'PROCESSING' CHECK (orderStatus IN ('PROCESSING', 'SHIPPED', 'DELIVERED', 'CANCELLED'))
);
GO

-- Create OrderDetails table
CREATE TABLE OrderDetails (
    orderDetailID INT PRIMARY KEY IDENTITY(1,1),
    orderID INT NOT NULL FOREIGN KEY REFERENCES Orders(orderID),
    productID INT NOT NULL FOREIGN KEY REFERENCES Products(productID),
    quantity INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL
);
GO

-- Insert sample data into Users table
INSERT INTO Users (userID, password, fullName, email, phone, address, role, status)
VALUES 
    ('admin', '111111', 'Administrator', 'admin@blindboxstore.com', '1234567890', 'Admin Address', 'ADMIN', 1),
    ('user1', '222222', 'Quoc Bao', 'quocbao@gmail.com', '0987654320', 'User Address', 'USER', 1),
    ('user2', '333333', 'Tuan Anh', 'tuananh@gmail.com', '0987654321', 'User Address', 'USER', 1),
    ('user3', '444444', 'Gia Thinh', 'giathinh@gmail.com', '0987654323', 'User Address', 'USER', 1),
    ('user4', '555555', 'Minh Tuan', 'minhtuan@gmail.com', '0987654324', 'User Address', 'USER', 1);
GO

-- Insert sample data into Categories table
INSERT INTO Categories (categoryName, description)
VALUES 
    ('Labubu Classic', 'Original Labubu collectible blind box figures'),
    ('Labubu Halloween', 'Halloween special edition Labubu figures'),
    ('Labubu Winter', 'Winter and holiday themed Labubu figures'),
    ('Labubu Collabs', 'Collaboration series with other brands and artists'),
    ('Labubu Minis', 'Miniature sized Labubu collectibles');
GO

-- Insert sample data into Products table (30 products)
INSERT INTO Products (productName, series, description, price, quantity, imageUrl, categoryID, createDate, status)
VALUES
    -- Labubu Classic Series
    ('Labubu Original Blue', 'Classic', 'Original blue Labubu figure with classic design', 29.99, 50, '/images/labubu-original-blue.jpg', 1, '2025-01-15', 1),
    ('Labubu Original Red', 'Classic', 'Original red Labubu figure with classic design', 29.99, 45, '/images/labubu-original-red.jpg', 1, '2025-01-15', 1),
    ('Labubu Original Green', 'Classic', 'Original green Labubu figure with classic design', 29.99, 40, '/images/labubu-original-green.jpg', 1, '2025-01-15', 1),
    ('Labubu Original Yellow', 'Classic', 'Original yellow Labubu figure with classic design', 29.99, 35, '/images/labubu-original-yellow.jpg', 1, '2025-01-15', 1),
    ('Labubu Original Purple', 'Classic', 'Original purple Labubu figure with classic design', 29.99, 30, '/images/labubu-original-purple.jpg', 1, '2025-01-15', 1),
    ('Labubu Original Black', 'Classic', 'Original black Labubu figure with classic design', 29.99, 25, '/images/labubu-original-black.jpg', 1, '2025-01-15', 1),
    
    -- Labubu Halloween Series
    ('Labubu Pumpkin', 'Halloween', 'Halloween pumpkin-themed Labubu figure', 34.99, 40, '/images/labubu-pumpkin.jpg', 2, '2025-02-01', 1),
    ('Labubu Vampire', 'Halloween', 'Halloween vampire-themed Labubu figure', 34.99, 35, '/images/labubu-vampire.jpg', 2, '2025-02-01', 1),
    ('Labubu Witch', 'Halloween', 'Halloween witch-themed Labubu figure', 34.99, 30, '/images/labubu-witch.jpg', 2, '2025-02-01', 1),
    ('Labubu Ghost', 'Halloween', 'Halloween ghost-themed Labubu figure', 34.99, 25, '/images/labubu-ghost.jpg', 2, '2025-02-01', 1),
    ('Labubu Zombie', 'Halloween', 'Halloween zombie-themed Labubu figure', 34.99, 20, '/images/labubu-zombie.jpg', 2, '2025-02-01', 1),
    ('Labubu Mummy', 'Halloween', 'Halloween mummy-themed Labubu figure', 34.99, 15, '/images/labubu-mummy.jpg', 2, '2025-02-01', 1),
    
    -- Labubu Winter Series
    ('Labubu Snowman', 'Winter', 'Winter snowman-themed Labubu figure', 34.99, 40, '/images/labubu-snowman.jpg', 3, '2025-02-15', 1),
    ('Labubu Santa', 'Winter', 'Winter Santa-themed Labubu figure', 34.99, 35, '/images/labubu-santa.jpg', 3, '2025-02-15', 1),
    ('Labubu Reindeer', 'Winter', 'Winter reindeer-themed Labubu figure', 34.99, 30, '/images/labubu-reindeer.jpg', 3, '2025-02-15', 1),
    ('Labubu Penguin', 'Winter', 'Winter penguin-themed Labubu figure', 34.99, 25, '/images/labubu-penguin.jpg', 3, '2025-02-15', 1),
    ('Labubu Polar Bear', 'Winter', 'Winter polar bear-themed Labubu figure', 34.99, 20, '/images/labubu-polar-bear.jpg', 3, '2025-02-15', 1),
    ('Labubu Ice Queen', 'Winter', 'Winter ice queen-themed Labubu figure', 34.99, 15, '/images/labubu-ice-queen.jpg', 3, '2025-02-15', 1),
    
    -- Labubu Collabs Series
    ('Labubu x Kaws', 'Collabs', 'Special collaboration with Kaws artist', 49.99, 30, '/images/labubu-kaws.jpg', 4, '2025-03-01', 1),
    ('Labubu x Sanrio', 'Collabs', 'Special collaboration with Sanrio characters', 49.99, 25, '/images/labubu-sanrio.jpg', 4, '2025-03-01', 1),
    ('Labubu x Disney', 'Collabs', 'Special collaboration with Disney characters', 49.99, 20, '/images/labubu-disney.jpg', 4, '2025-03-01', 1),
    ('Labubu x Marvel', 'Collabs', 'Special collaboration with Marvel characters', 49.99, 15, '/images/labubu-marvel.jpg', 4, '2025-03-01', 1),
    ('Labubu x Star Wars', 'Collabs', 'Special collaboration with Star Wars characters', 49.99, 10, '/images/labubu-star-wars.jpg', 4, '2025-03-01', 1),
    ('Labubu x BTS', 'Collabs', 'Special collaboration with BTS', 49.99, 5, '/images/labubu-bts.jpg', 4, '2025-03-01', 1),
    
    -- Labubu Minis Series
    ('Labubu Mini Blue', 'Minis', 'Miniature blue Labubu figure', 19.99, 50, '/images/labubu-mini-blue.jpg', 5, '2025-03-15', 1),
    ('Labubu Mini Red', 'Minis', 'Miniature red Labubu figure', 19.99, 45, '/images/labubu-mini-red.jpg', 5, '2025-03-15', 1),
    ('Labubu Mini Green', 'Minis', 'Miniature green Labubu figure', 19.99, 40, '/images/labubu-mini-green.jpg', 5, '2025-03-15', 1),
    ('Labubu Mini Yellow', 'Minis', 'Miniature yellow Labubu figure', 19.99, 35, '/images/labubu-mini-yellow.jpg', 5, '2025-03-15', 1),
    ('Labubu Mini Purple', 'Minis', 'Miniature purple Labubu figure', 19.99, 30, '/images/labubu-mini-purple.jpg', 5, '2025-03-15', 1),
    ('Labubu Mini Black', 'Minis', 'Miniature black Labubu figure', 19.99, 25, '/images/labubu-mini-black.jpg', 5, '2025-03-15', 1);
GO

-- Insert sample data into Orders table
INSERT INTO Orders (userID, orderDate, totalAmount, customerName, customerEmail, customerPhone, customerAddress, paymentMethod, paymentStatus, orderStatus)
VALUES
    (NULL, '2025-03-10 10:15:30', 59.98, 'Anna Nguyen', 'anna@gmail.com', '0337279741', 'FPT HCM', 'PAYPAL', 'COMPLETED', 'DELIVERED'),
    (NULL, '2025-03-11 14:22:45', 34.99, 'Binh Tran', 'binh@gmail.com', '0337279742', 'FPT HCM', 'CASH', 'PENDING', 'PROCESSING'),
    (NULL, '2025-03-12 16:30:10', 69.98, 'Cuong Le', 'cuong@gmail.com', '0337279743', 'FPT HCM', 'CREDIT_CARD', 'COMPLETED', 'SHIPPED'),
    ('user1', '2025-03-13 09:45:22', 79.97, 'Quoc Bao', 'quocbao@gmail.com', '0987654320', 'User Address', 'CREDIT_CARD', 'COMPLETED', 'PROCESSING'),
    ('user2', '2025-03-14 11:20:33', 149.97, 'Tuan Anh', 'tuananh@gmail.com', '0987654321', 'User Address', 'PAYPAL', 'COMPLETED', 'PROCESSING');
GO

-- Insert sample data into OrderDetails table
INSERT INTO OrderDetails (orderID, productID, quantity, price)
VALUES
    (1, 1, 1, 29.99),
    (1, 2, 1, 29.99),
    (2, 7, 1, 34.99),
    (3, 13, 1, 34.99),
    (3, 14, 1, 34.99),
    (4, 19, 1, 49.99),
    (4, 25, 1, 19.99),
    (4, 26, 1, 19.99),
    (5, 19, 1, 49.99),
    (5, 20, 1, 49.99),
    (5, 21, 1, 49.99);
GO

-- Create a view for product inventory
CREATE VIEW vw_ProductInventory AS
SELECT 
    p.productID,
    p.productName,
    p.series,
    c.categoryName,
    p.price,
    p.quantity AS StockQuantity,
    p.imageUrl,
    p.status
FROM Products p
JOIN Categories c ON p.categoryID = c.categoryID
WHERE p.status = 1;
GO

-- Create a view for order summary
CREATE VIEW vw_OrderSummary AS
SELECT 
    o.orderID,
    o.orderDate,
    o.totalAmount,
    o.customerName,
    o.customerEmail,
    o.paymentMethod,
    o.paymentStatus,
    o.orderStatus,
    COUNT(od.productID) AS TotalItems,
    u.userID,
    u.fullName AS UserFullName
FROM Orders o
LEFT JOIN Users u ON o.userID = u.userID
JOIN OrderDetails od ON o.orderID = od.orderID
GROUP BY 
    o.orderID,
    o.orderDate,
    o.totalAmount,
    o.customerName,
    o.customerEmail,
    o.paymentMethod,
    o.paymentStatus,
    o.orderStatus,
    u.userID,
    u.fullName;
GO

-- Stored Procedure: Get Product Details by ID
CREATE PROCEDURE sp_GetProductDetails
    @productID INT
AS
BEGIN
    SELECT 
        p.productID,
        p.productName,
        p.series,
        p.description,
        p.price,
        p.quantity,
        p.imageUrl,
        c.categoryName,
        p.createDate,
        p.lastUpdateDate,
        p.lastUpdateUser,
        p.status
    FROM Products p
    JOIN Categories c ON p.categoryID = c.categoryID
    WHERE p.productID = @productID;
END;
GO

-- Stored Procedure: Get User Details by ID
CREATE PROCEDURE sp_GetUserDetails
    @userID VARCHAR(50)
AS
BEGIN
    SELECT 
        userID,
        fullName,
        email,
        phone,
        address,
        role,
        status
    FROM Users
    WHERE userID = @userID;
END;
GO

-- Stored Procedure: Update Order Status
CREATE PROCEDURE sp_UpdateOrderStatus
    @orderID INT,
    @orderStatus VARCHAR(20)
AS
BEGIN
    UPDATE Orders
    SET orderStatus = @orderStatus
    WHERE orderID = @orderID;
END;
GO

-- Stored Procedure: Get Order Details by Order ID
CREATE PROCEDURE sp_GetOrderDetails
    @orderID INT
AS
BEGIN
    SELECT 
        od.orderDetailID,
        od.orderID,
        p.productName,
        od.quantity,
        od.price
    FROM OrderDetails od
    JOIN Products p ON od.productID = p.productID
    WHERE od.orderID = @orderID;
END;
GO

-- Stored Procedure: Get Revenue Report by Date Range
CREATE PROCEDURE sp_GetRevenueReport
    @startDate DATE,
    @endDate DATE
AS
BEGIN
    SELECT 
        o.orderDate,
        SUM(o.totalAmount) AS totalRevenue
    FROM Orders o
    WHERE o.orderDate BETWEEN @startDate AND @endDate
    AND o.paymentStatus = 'COMPLETED'
    GROUP BY o.orderDate
    ORDER BY o.orderDate ASC;
END;
GO