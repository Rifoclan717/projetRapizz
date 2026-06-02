create table Ingredients(
    id int primary key not null auto_increment,
    name varchar(255) not null,
    unique key uq_ingredients_name (name)
);

create table Products(
    id int primary key not null auto_increment,
    name varchar(255) not null,
    basePrice decimal(5,2) not null,
    key idx_products_name (name)
);

create table ProductIngredient(
    ingredientId int not null,
    productId int not null,
    primary key (ingredientId, productId),
    key idx_productingredient_productId (productId),
    constraint fk_productingredient_ingredient
        foreign key (ingredientId) references Ingredients(id) on delete cascade,
    constraint fk_productingredient_product
        foreign key (productId) references Products(id) on delete cascade
);

create table Clients(
    id int primary key not null auto_increment,
    email varchar(320) not null,
    phoneNumber varchar(32),
    firstName varchar(100),
    lastName varchar(100),
    address varchar(500),
    password_hash varchar(255) not null,
    balance DECIMAL(10,2) NOT NULL DEFAULT 0.00, 
    unique key uq_clients_email (email),
    key idx_clients_phoneNumber (phoneNumber)
);

create table Drivers(
    id int primary key not null auto_increment,
    firstName varchar(100),
    lastName varchar(100),
    canBike boolean not null default false,
    canDrive boolean not null default false
);

create table Vehicles(
    id int primary key not null auto_increment,
    plateNumber varchar(32) not null,
    brand varchar(100),
    model varchar(100),
    type enum('bike','car') not null,
    unique key uq_vehicles_plateNumber (plateNumber),
    key idx_vehicles_type (type)
);

create table Orders(
    id int primary key not null auto_increment,
    deliveryTime datetime,
    orderDate datetime not null,
    tenthOfGift boolean not null default false,
    clientId int not null,
    driverId int not null,
    vehicleId int not null,
    key idx_orders_clientId (clientId),
    key idx_orders_driverId (driverId),
    key idx_orders_vehicleId (vehicleId),
    key idx_orders_orderDate (orderDate),
    constraint fk_orders_client foreign key (clientId) references Clients(id) on delete cascade,
    constraint fk_orders_driver foreign key (driverId) references Drivers(id) on delete cascade,
    constraint fk_orders_vehicle foreign key (vehicleId) references Vehicles(id) on delete cascade
);

create table OrderProduct(
    orderId int not null,
    productId int not null,
    quantity int not null,
    size enum('naine','humaine','ogresse') not null,
    primary key (orderId, productId),
    key idx_orderproduct_productId (productId),
    constraint chk_orderproduct_quantity_positive check (quantity >= 1),
    constraint fk_orderproduct_order foreign key (orderId) references Orders(id) on delete cascade,
    constraint fk_orderproduct_product foreign key (productId) references Products(id) on delete cascade
);

ALTER TABLE OrderProduct ADD COLUMN size ENUM('naine','humaine','ogresse') NOT NULL DEFAULT 'humaine';
