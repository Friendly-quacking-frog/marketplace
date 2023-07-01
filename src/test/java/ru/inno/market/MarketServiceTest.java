package ru.inno.market;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.inno.market.core.Catalog;
import ru.inno.market.core.MarketService;
import ru.inno.market.model.Client;
import ru.inno.market.model.Item;
import ru.inno.market.model.PromoCodes;

public class MarketServiceTest {
    private final MarketService marketService = new MarketService();
    private final Catalog catalog = new Catalog();
    private final Client client = new Client(1, "Alex");
    private int orderId;

    @BeforeEach
    public void setUp(){
        orderId = marketService.createOrderFor(client);
    }

    @Test
    @DisplayName("Проверка создания заказа для клиента")
    public void testCreateOrderFor(){
        //Создаем экземпляр клиента по id нашего заказа
        Client testClient = marketService.getOrderInfo(orderId).getClient();
        //Проверяем что наш и полученный клиенты совпадают
        assert client.equals(testClient);
    }

    @Test
    @DisplayName("Проверка добавления предмета в заказ")
    public void testAddItemToOrder(){
        //Создаем Item по нашему itemId
        Item testItem = catalog.getItemById(3);
        //Добавляем Item в заказ по orderId
        marketService.addItemToOrder(testItem, orderId);
        //Проверяем наличие testItem в заказе по orderId
        assert marketService.getOrderInfo(orderId).getItems().keySet().contains(testItem);
    }

    @Test
    @DisplayName("Проверка применения скидки к заказу")
    public void testApplyDiscountForOrder(){
        //Создадим несколько экзмеляров Item
        Item testItem1 = catalog.getItemById(6);
        Item testItem2 = catalog.getItemById(8);
        //Рассчитаем общую сумму заказа до применения скидки
        double total = testItem2.getPrice() + testItem1.getPrice();
        //Добавим Item в заказ
        marketService.addItemToOrder(testItem1, orderId);
        marketService.addItemToOrder(testItem2, orderId);
        //Применим скидку к заказу
        marketService.applyDiscountForOrder(orderId, PromoCodes.FIRST_ORDER);
        //Вычислим множитель для суммы заказа
        double multiplier = 1 - PromoCodes.FIRST_ORDER.getDiscount();
        //Пересчитаем сумму заказа
        total *= multiplier;
        //Проверяем что рассичтанная сумма равна сумме заказа
        assert total == marketService.getOrderInfo(orderId).getTotalPrice();
    }
}
