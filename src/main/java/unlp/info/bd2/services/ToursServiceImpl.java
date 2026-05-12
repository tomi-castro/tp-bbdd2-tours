package unlp.info.bd2.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unlp.info.bd2.repositories.jpa.*;
import unlp.info.bd2.model.DriverUser;
import unlp.info.bd2.model.ItemService;
import unlp.info.bd2.model.Purchase;
import unlp.info.bd2.model.Review;
import unlp.info.bd2.model.Route;
import unlp.info.bd2.model.Stop;
import unlp.info.bd2.model.Supplier;
import unlp.info.bd2.model.TourGuideUser;
import unlp.info.bd2.model.User;
import unlp.info.bd2.repositories.ToursRepository;
import unlp.info.bd2.utils.ToursException;

@Service
public class ToursServiceImpl implements ToursService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DriverUserRepository driverUserRepository;
    @Autowired
    private TourGuideUserRepository tourGuideUserRepository;

    @Autowired
    private StopRepository stopRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private ItemServiceRepository itemServiceRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public User createUser(String username, String password, String fullName, String email, Date birthdate, String phoneNumber) throws ToursException {
            if (alreadyRegisteredName(username)) {
                throw new ToursException("Username already exists: " + username);
            }
        User user = new User(username, password, fullName, email, birthdate, phoneNumber);
        return userRepository.save(user);
    }

    @Override
    public DriverUser createDriverUser(String username, String password, String fullName, String email, Date birthdate,
            String phoneNumber, String expedient) throws ToursException {
        if (alreadyRegisteredName(username)) {
                throw new ToursException("Username already exists: " + username);
            }
        DriverUser driverUser = new DriverUser(username, password, fullName, email, birthdate, phoneNumber, expedient);
        return driverUserRepository.save(driverUser);
    }

    @Override
    public TourGuideUser createTourGuideUser(String username, String password, String fullName, String email,
            Date birthdate, String phoneNumber, String education) throws ToursException {
            if (alreadyRegisteredName(username)) {
                throw new ToursException("Username already exists: " + username);
            }
        TourGuideUser tourGuideUser = new TourGuideUser(username, password, fullName, email, birthdate, phoneNumber, education);
        return tourGuideUserRepository.save(tourGuideUser);
    }

    @Override
    public Optional<User> getUserById(Long id) throws ToursException {
        return userRepository.findById(id).filter(User::isActive);
    }

    @Override
    public Optional<User> getUserByUsername(String username) throws ToursException {
        Optional<User> user = userRepository.findByUsername(username).filter(User::isActive);
        return user;
    }

    @Override
    public User updateUser(User user) throws ToursException {
        User userWithUsername = userRepository.findByUsername(user.getUsername()).orElse(null);
        if (userWithUsername != null && !userWithUsername.getId().equals(user.getId())) {
            throw new ToursException("Username already exists: " + user.getUsername());
        }
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(User user) throws ToursException {
        if (!user.canBeDeleted()) {
            throw new ToursException("El usuario se encuentra desactivado");
        }
        user.setActive(false);
        this.updateUser(user);
    }

    @Override
    public Stop createStop(String name, String description) throws ToursException {
        return stopRepository.save(new Stop(name, description));
    }

    @Override
    public List<Stop> getStopByNameStart(String name) {
        return stopRepository.findByNameStartingWith(name);
    }

    @Override
    public Route createRoute(String name, float price, float totalKm, int maxNumberOfUsers, List<Stop> stops)
            throws ToursException {
        Route route = new Route(name, price, totalKm, maxNumberOfUsers, stops);
        return routeRepository.save(route);
    }

    @Override
    public Optional<Route> getRouteById(Long id) {
        return routeRepository.findById(id);
    }

    @Override
    public List<Route> getRoutesBelowPrice(float price) {
        return routeRepository.findByPriceLessThan(price);
        
    }

    @Override
    public void assignDriverByUsername(String username, Long idRoute) throws ToursException {
        DriverUser user = driverUserRepository.findByUsername(username)
            .orElseThrow(() -> new ToursException("Driver not found with username: " + username));
        Route route = routeRepository.findById(idRoute)
            .orElseThrow(() -> new ToursException("Route not found with id: " + idRoute));
        DriverUser driverUser = (DriverUser) user;
        driverUser.addRoute(route);
        routeRepository.save(route);
        driverUserRepository.save(driverUser);
    }

    @Override
    public void assignTourGuideByUsername(String username, Long idRoute) throws ToursException {
        TourGuideUser user = tourGuideUserRepository.findByUsername(username)
            .orElseThrow(() -> new ToursException("Tour guide not found with username: " + username));
        Route route = routeRepository.findById(idRoute)
            .orElseThrow(() -> new ToursException("Route not found with id: " + idRoute));
        TourGuideUser tourGuideUser = (TourGuideUser) user;
        tourGuideUser.addRoute(route);
        routeRepository.save(route);
        tourGuideUserRepository.save(tourGuideUser);
    }

    @Override
    public Supplier createSupplier(String businessName, String authorizationNumber) throws ToursException {
        if (supplierRepository.findByAuthorizationNumber(authorizationNumber).isPresent()) {
            throw new ToursException("Supplier with authorization number " + authorizationNumber + " already exists");
        }
        Supplier supplier = new Supplier(businessName, authorizationNumber);
        return supplierRepository.save(supplier);
    }

    @Override
    public unlp.info.bd2.model.Service addServiceToSupplier(String name, float price, String description,
            Supplier supplier) throws ToursException {
        unlp.info.bd2.model.Service service = new unlp.info.bd2.model.Service(name, price, description, supplier);
        service.setSupplier(supplier);
        supplier.addService(service);
        return serviceRepository.save(service);
}

    @Override
    public unlp.info.bd2.model.Service updateServicePriceById(Long id, float newPrice) throws ToursException {
        unlp.info.bd2.model.Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new ToursException("Service not found with id: " + id));
        service.setPrice(newPrice);
        return serviceRepository.save(service);
    }

    @Override
    public Optional<Supplier> getSupplierById(Long id) {
        return supplierRepository.findById(id);
    }

    @Override
    public Optional<Supplier> getSupplierByAuthorizationNumber(String authorizationNumber) {
        return supplierRepository.findByAuthorizationNumber(authorizationNumber);
    }

    @Override
    public Optional<unlp.info.bd2.model.Service> getServiceByNameAndSupplierId(String name, Long id)
        throws ToursException {
        return serviceRepository.findByNameAndSupplierId(name, id);
    }

    @Override
    public Purchase createPurchase(String code, Route route, User user) throws ToursException {
        Purchase purchase = new Purchase(code, route, user);
        return purchaseRepository.save(purchase);
    }
    @Override
    public Purchase createPurchase(String code, Date date, Route route, User user) throws ToursException {
        Purchase purchase = new Purchase(code, date, route, user);
        user.addPurchase(purchase);
        return purchaseRepository.save(purchase);
    }

    @Override
    public ItemService addItemToPurchase(unlp.info.bd2.model.Service service, int quantity, Purchase purchase)
            throws ToursException {
        ItemService itemService = new ItemService(service, quantity, purchase);
        purchase.addItemService(itemService);
        service.addItemService(itemService);
        return itemServiceRepository.save(itemService);
    }

    @Override
    public Optional<Purchase> getPurchaseByCode(String code) {
        return purchaseRepository.findByCode(code);
    }

    @Override
    public void deletePurchase(Purchase purchase) throws ToursException {
        purchaseRepository.delete(purchase);
    }

    @Override
    public Review addReviewToPurchase(int rating, String comment, Purchase purchase) throws ToursException {
        Review review = new Review(rating, comment, purchase);
        purchase.setReview(review);
        return reviewRepository.save(review);
    }

    @Override
    public void deleteRoute(Route route) throws ToursException {
        routeRepository.delete(route);
    }

    @Override
    public List<Purchase> getAllPurchasesOfUsername(String username) {
        return purchaseRepository.findByUserUsername(username);
    }

    @Override
    public List<User> getUserSpendingMoreThan(float mount) {
        return purchaseRepository.findUsersSpendingMoreThan(mount);
    }

    @Override
    public List<Supplier> getTopNSuppliersInPurchases(int n) {
        return purchaseRepository.findTopSuppliersInPurchases(org.springframework.data.domain.PageRequest.of(0, n));
    }

    @Override
    public long getCountOfPurchasesBetweenDates(Date start, Date end) {
        return purchaseRepository.countPurchasesBetweenDates(start, end);
    }

    @Override
    public List<Route> getRoutesWithStop(Stop stop) {
        return routeRepository.findRoutesWithStop(stop.getId());
    }

    @Override
    public Long getMaxStopOfRoutes() {
        return routeRepository.findMaxStopOfRoutes();
    }

    @Override
    public List<Route> getRoutsNotSell() {
        return routeRepository.findRoutsNotSell();
    }

    @Override
    public List<Route> getTop3RoutesWithMaxRating() {
        return routeRepository.findTop3RoutesWithMaxRating(org.springframework.data.domain.PageRequest.of(0, 3));
    }

    @Override
    public unlp.info.bd2.model.Service getMostDemandedService() {
        return serviceRepository.findMostDemandedService(org.springframework.data.domain.PageRequest.of(0, 1))
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<TourGuideUser> getTourGuidesWithRating1() {
        return userRepository.findTourGuidesWithRating1();
    }

    private boolean alreadyRegisteredName(String username) {
        return userRepository.findByUsername(username).isPresent();

    }
    
    
}
