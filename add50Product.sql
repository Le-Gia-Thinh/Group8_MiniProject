-- Insert Categories for Teddy Products
INSERT INTO Categories (categoryName, description)
VALUES 
    ('Classic Teddy', 'Traditional and timeless teddy bear collection'),
    ('Holiday Teddy', 'Seasonal and festive themed teddy bears'),
    ('Giant Teddy', 'Extra large and oversized teddy bears'),
    ('Special Edition Teddy', 'Limited and collectible teddy bears'),
    ('Mini Teddy', 'Small and portable teddy bears'),
    ('Nature Themed Teddy', 'Teddy bears inspired by natural environments'),
    ('Professional Teddy', 'Teddy bears representing various professions'),
    ('Music Themed Teddy', 'Teddy bears celebrating musical genres and performers');
GO

-- Now insert the Teddy products
INSERT INTO Products (
    productName, 
    series, 
    description, 
    price, 
    quantity, 
    imageUrl, 
    categoryID, 
    createDate, 
    lastUpdateDate, 
    lastUpdateUser, 
    status
)
VALUES
    ('Classic Brown Teddy', 'Classic', 'Traditional soft brown teddy bear with embroidered nose', 24.99, 100, 'assets/images/classic-brown-teddy.jpg', 1, '2025-01-15', NULL, NULL, 1),
    ('Classic White Teddy', 'Classic', 'Soft plush white teddy bear with button eyes', 24.99, 90, 'assets/images/classic-white-teddy.jpg', 1, '2025-01-15', NULL, NULL, 1),
    ('Classic Beige Teddy', 'Classic', 'Neutral beige teddy bear with gentle smile', 24.99, 85, 'assets/images/classic-beige-teddy.jpg', 1, '2025-01-15', NULL, NULL, 1),
    ('Classic Gray Teddy', 'Classic', 'Sophisticated gray teddy bear with silk ribbon', 24.99, 80, 'assets/images/classic-gray-teddy.jpg', 1, '2025-01-15', NULL, NULL, 1),
    ('Classic Mocha Teddy', 'Classic', 'Rich mocha-colored teddy bear with soft fur', 24.99, 75, 'assets/images/classic-mocha-teddy.jpg', 1, '2025-01-15', NULL, NULL, 1),

    ('Christmas Santa Teddy', 'Holiday', 'Festive teddy bear wearing Santa outfit', 29.99, 60, 'assets/images/christmas-santa-teddy.jpg', 2, '2025-02-01', NULL, NULL, 1),
    ('Valentine Heart Teddy', 'Holiday', 'Red teddy bear with heart-shaped patch', 29.99, 55, 'assets/images/valentine-heart-teddy.jpg', 2, '2025-02-01', NULL, NULL, 1),
    ('Easter Bunny Teddy', 'Holiday', 'Pastel teddy bear with bunny ears', 29.99, 50, 'assets/images/easter-bunny-teddy.jpg', 2, '2025-02-01', NULL, NULL, 1),
    ('Halloween Witch Teddy', 'Holiday', 'Spooky teddy bear in witch costume', 29.99, 45, 'assets/images/halloween-witch-teddy.jpg', 2, '2025-02-01', NULL, NULL, 1),
    ('New Year Party Teddy', 'Holiday', 'Glittery teddy bear with party hat', 29.99, 40, 'assets/images/new-year-party-teddy.jpg', 2, '2025-02-01', NULL, NULL, 1),

    ('Giant Caramel Teddy', 'Giant', 'Extra large caramel-colored teddy bear', 79.99, 20, 'assets/images/giant-caramel-teddy.jpg', 3, '2025-02-15', NULL, NULL, 1),
    ('Giant Chocolate Teddy', 'Giant', 'Massive chocolate brown teddy bear', 79.99, 18, 'assets/images/giant-chocolate-teddy.jpg', 3, '2025-02-15', NULL, NULL, 1),
    ('Giant Cream Teddy', 'Giant', 'Huge cream-colored teddy bear with soft plush', 79.99, 15, 'assets/images/giant-cream-teddy.jpg', 3, '2025-02-15', NULL, NULL, 1),
    ('Giant Blue Teddy', 'Giant', 'Large sky blue teddy bear for special occasions', 79.99, 12, 'assets/images/giant-blue-teddy.jpg', 3, '2025-02-15', NULL, NULL, 1),
    ('Giant Rainbow Teddy', 'Giant', 'Colorful rainbow-themed giant teddy bear', 79.99, 10, 'assets/images/giant-rainbow-teddy.jpg', 3, '2025-02-15', NULL, NULL, 1),

    ('Artist Collaboration Teddy', 'Special Edition', 'Limited edition teddy designed by renowned artist', 59.99, 30, 'assets/images/artist-collab-teddy.jpg', 4, '2025-03-01', NULL, NULL, 1),
    ('Museum Collection Teddy', 'Special Edition', 'Museum-quality vintage style teddy bear', 59.99, 25, 'assets/images/museum-collection-teddy.jpg', 4, '2025-03-01', NULL, NULL, 1),
    ('Royal Heritage Teddy', 'Special Edition', 'Elegant teddy bear with royal accessories', 59.99, 20, 'assets/images/royal-heritage-teddy.jpg', 4, '2025-03-01', NULL, NULL, 1),
    ('Luxury Gold Teddy', 'Special Edition', 'Premium gold-trimmed teddy bear', 59.99, 15, 'assets/images/luxury-gold-teddy.jpg', 4, '2025-03-01', NULL, NULL, 1),
    ('Diamond Jubilee Teddy', 'Special Edition', 'Commemorative teddy with diamond embellishments', 59.99, 10, 'assets/images/diamond-jubilee-teddy.jpg', 4, '2025-03-01', NULL, NULL, 1),

    ('Mini Brown Pocket Teddy', 'Mini', 'Adorable small brown teddy for traveling', 14.99, 120, 'assets/images/mini-brown-teddy.jpg', 5, '2025-03-15', NULL, NULL, 1),
    ('Mini White Pocket Teddy', 'Mini', 'Cute small white teddy perfect for kids', 14.99, 110, 'assets/images/mini-white-teddy.jpg', 5, '2025-03-15', NULL, NULL, 1),
    ('Mini Pink Pocket Teddy', 'Mini', 'Sweet small pink teddy with bow', 14.99, 100, 'assets/images/mini-pink-teddy.jpg', 5, '2025-03-15', NULL, NULL, 1),
    ('Mini Blue Pocket Teddy', 'Mini', 'Compact small blue teddy for backpacks', 14.99, 90, 'assets/images/mini-blue-teddy.jpg', 5, '2025-03-15', NULL, NULL, 1),
    ('Mini Green Pocket Teddy', 'Mini', 'Small green teddy for nature lovers', 14.99, 80, 'assets/images/mini-green-teddy.jpg', 5, '2025-03-15', NULL, NULL, 1),

    ('Forest Guardian Teddy', 'Nature', 'Woodland-inspired green teddy bear', 34.99, 50, 'assets/images/forest-guardian-teddy.jpg', 6, '2025-04-01', NULL, NULL, 1),
    ('Ocean Explorer Teddy', 'Nature', 'Blue marine-themed teddy bear', 34.99, 45, 'assets/images/ocean-explorer-teddy.jpg', 6, '2025-04-01', NULL, NULL, 1),
    ('Desert Traveler Teddy', 'Nature', 'Sandy brown desert-themed teddy', 34.99, 40, 'assets/images/desert-traveler-teddy.jpg', 6, '2025-04-01', NULL, NULL, 1),
    ('Mountain Climber Teddy', 'Nature', 'Gray rocky mountain-inspired teddy', 34.99, 35, 'assets/images/mountain-climber-teddy.jpg', 6, '2025-04-01', NULL, NULL, 1),
    ('Arctic Explorer Teddy', 'Nature', 'White polar-themed teddy bear', 34.99, 30, 'assets/images/arctic-explorer-teddy.jpg', 6, '2025-04-01', NULL, NULL, 1),

    ('Doctor Teddy', 'Professional', 'Teddy bear dressed in medical scrubs', 39.99, 40, 'assets/images/doctor-teddy.jpg', 7, '2025-04-15', NULL, NULL, 1),
    ('Pilot Teddy', 'Professional', 'Teddy bear wearing aviation uniform', 39.99, 35, 'assets/images/pilot-teddy.jpg', 7, '2025-04-15', NULL, NULL, 1),
    ('Teacher Teddy', 'Professional', 'Scholarly teddy with glasses and book', 39.99, 30, 'assets/images/teacher-teddy.jpg', 7, '2025-04-15', NULL, NULL, 1),
    ('Chef Teddy', 'Professional', 'Teddy bear in professional kitchen attire', 39.99, 25, 'assets/images/chef-teddy.jpg', 7, '2025-04-15', NULL, NULL, 1),
    ('Astronaut Teddy', 'Professional', 'Space-themed teddy in astronaut suit', 39.99, 20, 'assets/images/astronaut-teddy.jpg', 7, '2025-04-15', NULL, NULL, 1),

    ('Rock Star Teddy', 'Music', 'Teddy bear with electric guitar', 44.99, 35, 'assets/images/rock-star-teddy.jpg', 8, '2025-05-01', NULL, NULL, 1),
    ('Classical Musician Teddy', 'Music', 'Teddy bear holding violin', 44.99, 30, 'assets/images/classical-musician-teddy.jpg', 8, '2025-05-01', NULL, NULL, 1),
    ('Jazz Performer Teddy', 'Music', 'Cool jazz teddy with saxophone', 44.99, 25, 'assets/images/jazz-performer-teddy.jpg', 8, '2025-05-01', NULL, NULL, 1),
    ('Pop Star Teddy', 'Music', 'Glittery pop music themed teddy', 44.99, 20, 'assets/images/pop-star-teddy.jpg', 8, '2025-05-01', NULL, NULL, 1),
    ('Symphony Conductor Teddy', 'Music', 'Elegant teddy with conductor baton', 44.99, 15, 'assets/images/symphony-conductor-teddy.jpg', 8, '2025-05-01', NULL, NULL, 1);
GO

DELETE FROM Categories
WHERE categoryID IN (
    SELECT categoryID FROM (
        SELECT categoryID 
        FROM Categories
        WHERE categoryName IN (
            'Classic Teddy',
            'Holiday Teddy',
            'Giant Teddy',
            'Special Edition Teddy',
            'Mini Teddy',
            'Nature Themed Teddy',
            'Professional Teddy',
            'Music Themed Teddy'
        )
        AND categoryID > (SELECT MIN(categoryID) FROM Categories c2 WHERE c2.categoryName = Categories.categoryName)
    ) AS duplicates
);

UPDATE Products
SET categoryID = 
    CASE 
        WHEN series = 'Classic' THEN 6
        WHEN series = 'Holiday' THEN 7
        WHEN series = 'Giant' THEN 8
        WHEN series = 'Special Edition' THEN 9
        WHEN series = 'Mini' THEN 10
        WHEN series = 'Nature' THEN 11
        WHEN series = 'Professional' THEN 12
        WHEN series = 'Music' THEN 13
    END
WHERE productName LIKE '%Teddy%';