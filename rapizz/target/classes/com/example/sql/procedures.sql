DROP PROCEDURE IF EXISTS mark_order_delivered;

DELIMITER //

CREATE PROCEDURE mark_order_delivered(IN p_orderId INT)
BEGIN
    DECLARE v_orderDate DATETIME;
    DECLARE v_deliveryTime DATETIME;
    DECLARE v_clientId INT;
    DECLARE v_isGift BOOLEAN;
    DECLARE v_diff INT;
    DECLARE v_cost DECIMAL(10,2);
    DECLARE v_prod_price DECIMAL(5,2);
    DECLARE v_size VARCHAR(20);
    DECLARE v_quantity INT;
    DECLARE v_mult DECIMAL(5,4);

    -- Récupérer les infos commande
    SELECT orderDate, deliveryTime, clientId, tenthOfGift
    INTO v_orderDate, v_deliveryTime, v_clientId, v_isGift
    FROM Orders
    WHERE id = p_orderId;

    IF v_deliveryTime IS NULL THEN

        UPDATE Orders
        SET deliveryTime = NOW()
        WHERE id = p_orderId;

        SELECT deliveryTime
        INTO v_deliveryTime
        FROM Orders
        WHERE id = p_orderId;

        SET v_diff = TIMESTAMPDIFF(MINUTE, v_orderDate, v_deliveryTime);

        IF v_diff >= 30 AND v_isGift = FALSE THEN

            SELECT op.quantity, op.size, p.basePrice
            INTO v_quantity, v_size, v_prod_price
            FROM OrderProduct op
            JOIN Products p ON op.productId = p.id
            WHERE op.orderId = p_orderId
            LIMIT 1;

            IF v_size = 'naine' THEN
                SET v_mult = 0.6667;
            ELSEIF v_size = 'humaine' THEN
                SET v_mult = 1.0000;
            ELSEIF v_size = 'ogresse' THEN
                SET v_mult = 1.3333;
            END IF;

            SET v_cost = v_prod_price * v_mult * v_quantity;

            UPDATE Clients
            SET balance = balance + v_cost
            WHERE id = v_clientId;

        END IF;
    END IF;
END//

DELIMITER ;