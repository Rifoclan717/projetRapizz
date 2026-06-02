DROP TRIGGER IF EXISTS trg_orders_before_insert;
DROP TRIGGER IF EXISTS trg_orderproduct_after_insert;

DELIMITER //

-- Gestion de la fidélité : définir si c'est la 10ème commande
CREATE TRIGGER trg_orders_before_insert
BEFORE INSERT ON Orders
FOR EACH ROW
BEGIN
    DECLARE order_count INT;
    
    -- Compter combien de commandes le client a déjà passées
    SELECT COUNT(*) INTO order_count FROM Orders WHERE clientId = NEW.clientId;
    
    -- Si le client a déjà 9 commandes (ou un multiple de 9+1 etc), la 10ème est gratuite
    IF (order_count + 1) % 10 = 0 THEN
        SET NEW.tenthOfGift = TRUE;
    ELSE
        SET NEW.tenthOfGift = FALSE;
    END IF;
END //

-- Rembourser automatiquement si la commande nouvellement insérée est une commande gratuite (fidélité)
-- Étant donné que le système Java a déjà débité le solde du client avant l'insertion !
CREATE TRIGGER trg_orderproduct_after_insert
AFTER INSERT ON OrderProduct
FOR EACH ROW
BEGIN
    DECLARE is_gift BOOLEAN;
    DECLARE c_id INT;
    DECLARE order_cost DECIMAL(10,2);
    DECLARE prod_price DECIMAL(5,2);
    DECLARE mult DECIMAL(5,4);

    -- Vérifier si la commande correspondante est marquée comme un cadeau
    SELECT tenthOfGift, clientId INTO is_gift, c_id FROM Orders WHERE id = NEW.orderId;

    IF is_gift THEN
        -- Retrouver le prix du produit
        SELECT basePrice INTO prod_price FROM Products WHERE id = NEW.productId;
        
        IF NEW.size = 'naine' THEN SET mult = 0.6667;
        ELSEIF NEW.size = 'humaine' THEN SET mult = 1.0000;
        ELSEIF NEW.size = 'ogresse' THEN SET mult = 1.3333;
        END IF;
        
        SET order_cost = prod_price * mult * NEW.quantity;
        
        -- Recréditer le solde du client car c'était la 10ème commande gratuite
        UPDATE Clients SET balance = balance + order_cost WHERE id = c_id;
    END IF;
END //

DELIMITER ;