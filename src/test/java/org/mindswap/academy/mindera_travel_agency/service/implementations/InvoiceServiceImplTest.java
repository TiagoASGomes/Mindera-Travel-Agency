package org.mindswap.academy.mindera_travel_agency.service.implementations;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceImplTest {
//
//    @Mock
//    private InvoiceRepository invoiceRepository;
//    @Mock
//    private InvoiceConverter invoiceConverter;
//    @Mock
//    private PaymentStatusService paymentStatusService;
//    @Mock
//    private UserService userService;
//    @InjectMocks
//    private InvoiceServiceImpl invoiceService;
//
//    private User user;
//    private Invoice invoice;
//    private InvoiceGetDto invoiceGetDto;
//    private InvoiceCreateDto invoiceCreateDto;
//    private InvoiceUpdateDto invoiceUpdateDto;
//
//    @BeforeEach
//    void setUp() {
//        InvoiceGetDto invoiceGetDto = new InvoiceGetDto(1L, 0, null, "Not Requested", null, null);
//    }
//
//
//    @Test
//    void testwhenGetAllInvoices_thenReturnsCorrectPageSize() {
//        Pageable pageable = PageRequest.of(0, 5);
//        Page<Invoice> invoicePage = new PageImpl<>(new ArrayList<>());
//        when(invoiceRepository.findAll(pageable)).thenReturn(invoicePage);
//        Page<InvoiceGetDto> result = invoiceService.getAll(pageable);
//        assertEquals(invoicePage.getSize(), result.getSize());
//    }
//
//    @Test
//    void testwhenGetInvoiceById_thenReturnsCorrectInvoice() throws InvoiceNotFoundException {
//        Invoice invoice = new Invoice(new User());
//        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));
//        when(invoiceConverter.fromEntityToGetDto(invoice)).thenReturn(new InvoiceGetDto(1L, 0, null, "Not Requested", null, null));
//        InvoiceGetDto result = invoiceService.getById(1L);
//        assertEquals(1L, result.id());
//    }
//
//    @Test
//    void testwhenGetInvoiceByIdWithValidId_thenReturnsCorrectInvoice() throws InvoiceNotFoundException {
//        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));
//        when(invoiceConverter.fromEntityToGetDto(invoice)).thenReturn(invoiceGetDto);
//        InvoiceGetDto result = invoiceService.getById(1L);
//        assertEquals(1L, result.id());
//    }
//
//    @Test
//    void testGetInvoiceByIdWithDifferentValidId() throws InvoiceNotFoundException {
//        when(invoiceRepository.findById(2L)).thenReturn(Optional.of(invoice));
//        when(invoiceConverter.fromEntityToGetDto(invoice)).thenReturn(invoiceGetDto);
//        InvoiceGetDto result = invoiceService.getById(2L);
//        assertEquals(1L, result.id());
//    }
//
//    @Test
//    void testGetInvoiceByIdWithInvalidId() {
//        when(invoiceRepository.findById(3L)).thenReturn(Optional.empty());
//        assertThrows(InvoiceNotFoundException.class, () -> invoiceService.getById(3L));
//    }
//
//    @Test
//    void testGetInvoiceByIdWithAnotherInvalidId() {
//        when(invoiceRepository.findById(4L)).thenReturn(Optional.empty());
//        assertThrows(InvoiceNotFoundException.class, () -> invoiceService.getById(4L));
//    }
//
//    @Test
//    void testwhenCreateInvoice_thenReturnsCorrectInvoice() throws UserNotFoundException, PaymentStatusNotFoundException {
//        InvoiceCreateDto invoiceCreateDto = new InvoiceCreateDto(1L);
//        User user = new User();
//        Invoice invoice = new Invoice(user);
//        when(userService.findById(1L)).thenReturn(user);
//        when(paymentStatusService.findByName("Not Requested")).thenReturn(new PaymentStatus("Not Requested"));
//        when(invoiceConverter.fromCreateDtoToEntity(user)).thenReturn(invoice);
//        when(invoiceRepository.save(invoice)).thenReturn(invoice);
//        when(invoiceConverter.fromEntityToGetDto(invoice)).thenReturn(new InvoiceGetDto(1L, 0, null, "Not Requested", null, null));
//        InvoiceGetDto result = invoiceService.create(invoiceCreateDto);
//        assertEquals(1L, result.id());
//    }
//
//    @Test
//    void testCreateInvoiceWithInvalidUser() throws UserNotFoundException {
//        InvoiceCreateDto invoiceCreateDto = new InvoiceCreateDto(1L);
//        when(userService.findById(1L)).thenThrow(new UserNotFoundException("User not found"));
//        assertThrows(UserNotFoundException.class, () -> invoiceService.create(invoiceCreateDto));
//    }
//
//    @Test
//    void testCreateInvoiceWithInvalidPaymentStatus() throws UserNotFoundException, PaymentStatusNotFoundException {
//        when(userService.findById(1L)).thenReturn(user);
//        when(paymentStatusService.findByName("Invalid Status")).thenThrow(new PaymentStatusNotFoundException("Payment status not found"));
//        assertThrows(PaymentStatusNotFoundException.class, () -> invoiceService.create(invoiceCreateDto));
//    }
//
//    @Test
//    void testCreateInvoiceWithNonExistingInvoice() throws UserNotFoundException, PaymentStatusNotFoundException {
//        when(userService.findById(1L)).thenReturn(user);
//        when(paymentStatusService.findByName("Not Requested")).thenReturn(new PaymentStatus("Not Requested"));
//        when(invoiceRepository.save(any(Invoice.class))).thenThrow(new InvoiceNotFoundException("Invoice not found"));
//        assertThrows(InvoiceNotFoundException.class, () -> invoiceService.create(invoiceCreateDto));
//    }
//
//    @Test
//    void testCreateInvoiceWithAnotherInvalidUser() throws UserNotFoundException {
//        when(userService.findById(4L)).thenThrow(new UserNotFoundException("User not found"));
//        assertThrows(UserNotFoundException.class, () -> invoiceService.create(invoiceCreateDto));
//    }
//
//    @Test
//    void testCreateInvoiceWithIllegalArgument() throws UserNotFoundException, PaymentStatusNotFoundException {
//        when(userService.findById(1L)).thenReturn(user);
//        when(paymentStatusService.findByName("Not Requested")).thenReturn(new PaymentStatus("Not Requested"));
//        when(invoiceConverter.fromCreateDtoToEntity(any(User.class))).thenThrow(new IllegalArgumentException("Illegal argument provided"));
//        assertThrows(IllegalArgumentException.class, () -> invoiceService.create(invoiceCreateDto));
//    }
//
//    @Test
//    void testCreateInvoiceWithNullArgument() throws PaymentStatusNotFoundException, UserNotFoundException {
//        when(userService.findById(1L)).thenReturn(user);
//        when(paymentStatusService.findByName("Not Requested")).thenReturn(new PaymentStatus("Not Requested"));
//        when(invoiceConverter.fromCreateDtoToEntity(any(User.class))).thenThrow(new NullPointerException("Null argument provided"));
//        assertThrows(NullPointerException.class, () -> invoiceService.create(invoiceCreateDto));
//    }
//
//    @Test
//    void testwhenUpdateInvoice_thenReturnsUpdatedInvoice() throws InvoiceNotFoundException, PaymentStatusNotFoundException, PaymentCompletedException {
//        Invoice invoice = new Invoice(new User());
//        invoice.setPaymentStatus(new PaymentStatus("Not Paid"));
//        InvoiceUpdateDto invoiceUpdateDto = new InvoiceUpdateDto("Paid", LocalDateTime.now());
//        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));
//        when(paymentStatusService.findByName("Paid")).thenReturn(new PaymentStatus("Paid"));
//        when(invoiceRepository.save(invoice)).thenReturn(invoice);
//        when(invoiceConverter.fromEntityToGetDto(invoice)).thenReturn(new InvoiceGetDto(1L, 0, invoiceUpdateDto.paymentDate(), "Paid", null, null));
//        InvoiceGetDto result = invoiceService.update(1L, invoiceUpdateDto);
//        assertEquals("Paid", result.paymentStatus());
//    }
//
//    @Test
//    void testUpdateInvoiceWithNonExistingInvoice() {
//        InvoiceUpdateDto invoiceUpdateDto = new InvoiceUpdateDto("Paid", LocalDateTime.now());
//        when(invoiceRepository.findById(1L)).thenReturn(Optional.empty());
//        assertThrows(InvoiceNotFoundException.class, () -> invoiceService.update(1L, invoiceUpdateDto));
//    }
//
//    @Test
//    void testUpdateInvoiceWithNonExistingPaymentStatus() throws PaymentStatusNotFoundException {
//        Invoice invoice = new Invoice(new User());
//        invoice.setPaymentStatus(new PaymentStatus("Not Paid"));
//        InvoiceUpdateDto invoiceUpdateDto = new InvoiceUpdateDto("Invalid Status", LocalDateTime.now());
//        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));
//        when(paymentStatusService.findByName("Invalid Status")).thenThrow(new PaymentStatusNotFoundException("Payment status not found"));
//        assertThrows(PaymentStatusNotFoundException.class, () -> invoiceService.update(1L, invoiceUpdateDto));
//    }
//
//    @Test
//    void testUpdateInvoiceWithPaymentCompleted() throws PaymentStatusNotFoundException {
//        Invoice invoice = new Invoice(new User());
//        invoice.setPaymentStatus(new PaymentStatus("Paid"));
//        InvoiceUpdateDto invoiceUpdateDto = new InvoiceUpdateDto("Paid", LocalDateTime.now());
//        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));
//        when(paymentStatusService.findByName("Paid")).thenReturn(new PaymentStatus("Paid"));
//        assertThrows(PaymentCompletedException.class, () -> invoiceService.update(1L, invoiceUpdateDto));
//    }
//
//    @Test
//    void testUpdateInvoiceWithNullPaymentStatus() {
//        Invoice invoice = new Invoice(new User());
//        invoice.setPaymentStatus(new PaymentStatus("Not Paid"));
//        InvoiceUpdateDto invoiceUpdateDto = new InvoiceUpdateDto(null, LocalDateTime.now());
//        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));
//        assertThrows(IllegalArgumentException.class, () -> invoiceService.update(1L, invoiceUpdateDto));
//    }
//
//    @Test
//    void testUpdateInvoiceWithNullPaymentDate() throws PaymentStatusNotFoundException {
//        Invoice invoice = new Invoice(new User());
//        invoice.setPaymentStatus(new PaymentStatus("Not Paid"));
//        InvoiceUpdateDto invoiceUpdateDto = new InvoiceUpdateDto("Paid", null);
//        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));
//        when(paymentStatusService.findByName("Paid")).thenReturn(new PaymentStatus("Paid"));
//        assertThrows(IllegalArgumentException.class, () -> invoiceService.update(1L, invoiceUpdateDto));
//    }
//
//    @Test
//    void testUpdateInvoiceWithFuturePaymentDate() throws PaymentStatusNotFoundException {
//        Invoice invoice = new Invoice(new User());
//        invoice.setPaymentStatus(new PaymentStatus("Not Paid"));
//        InvoiceUpdateDto invoiceUpdateDto = new InvoiceUpdateDto("Paid", LocalDateTime.now().plusDays(1));
//        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));
//        when(paymentStatusService.findByName("Paid")).thenReturn(new PaymentStatus("Paid"));
//        assertThrows(IllegalArgumentException.class, () -> invoiceService.update(1L, invoiceUpdateDto));
//    }
//
//    @Test
//    void testDeleteInvoice() throws InvoiceNotFoundException, PaymentCompletedException {
//        Invoice invoice = new Invoice(new User());
//        invoice.setPaymentStatus(new PaymentStatus("Not Paid"));
//        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));
//        invoiceService.delete(1L);
//        verify(invoiceRepository, times(1)).deleteById(1L);
//    }
//
//    @Test
//    void testDeleteInvoiceWithNonExistingInvoice() {
//        when(invoiceRepository.findById(1L)).thenReturn(Optional.empty());
//        assertThrows(InvoiceNotFoundException.class, () -> invoiceService.delete(1L));
//    }
//
//    @Test
//    void testDeleteInvoiceWithPaymentCompleted() {
//        Invoice invoice = new Invoice(new User());
//        invoice.setPaymentStatus(new PaymentStatus("Paid"));
//        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));
//        assertThrows(PaymentCompletedException.class, () -> invoiceService.delete(1L));
//    }
//
//    @Test
//    void testDeleteInvoiceTwice() throws InvoiceNotFoundException, PaymentCompletedException {
//        Invoice invoice = new Invoice(new User());
//        invoice.setPaymentStatus(new PaymentStatus("Not Paid"));
//        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));
//        invoiceService.delete(1L);
//        assertThrows(InvoiceNotFoundException.class, () -> invoiceService.delete(1L));
//    }
//
//    @Test
//    void testDeleteInvoiceWithNullId() {
//        assertThrows(IllegalArgumentException.class, () -> invoiceService.delete(null));
//    }
//
//    @Test
//    void testDeleteInvoiceWithNegativeId() {
//        Long invoiceId = -1L;
//        assertThrows(IllegalArgumentException.class, () -> invoiceService.delete(invoiceId));
//    }
//
//    @Test
//    void testDeleteInvoiceWithZeroId() {
//        Long invoiceId = 0L;
//        assertThrows(IllegalArgumentException.class, () -> invoiceService.delete(invoiceId));
//    }

}
