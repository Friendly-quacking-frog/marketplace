package ru.inno.market;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.inno.market.core.Catalog;
import ru.inno.market.model.Client;
import ru.inno.market.model.Item;
import ru.inno.market.model.Order;
import ru.inno.market.model.PromoCodes;

public class OrderTest {
    private final Client client = new Client(1, "Alex");
    private final Catalog catalog =new Catalog();
    private Order order;

    @BeforeEach
    public void setUp(){
        order = new Order(1, client);
    }

    @Test
    @DisplayName("Проверка дообавления товара в заказ")
    public void tesAddItem(){
        //Создаем экзампляр Item
        Item testItem = catalog.getItemById(6);
        //Добавляем товар в заказ
        order.addItem(testItem);
        //Проверяем наличием товара в заказе
        assert order.getItems().keySet().contains(testItem);
    }

    @Test
    @DisplayName("Проверка применения скидки")
    public void testApplyDiscount(){
        //Получаем сумму до применения скидки
        double total = order.getTotalPrice();
        //Применяем скидку
        order.applyDiscount(PromoCodes.FIRST_ORDER.getDiscount());
        //Вычислим множитель для суммы заказа
        double multiplier = 1 - PromoCodes.FIRST_ORDER.getDiscount();
        //Пересчитаем сумму заказа
        total *= multiplier;
        //Проверяем что рассичтанная сумма равна сумме заказа
        assert total == order.getTotalPrice();
    }
}
