package org.mindswap.academy.mindera_travel_agency.service.implementations;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class PaymentStatusServiceImplTest {
//    @MockBean
//    private PaymentStatusRepository paymentStatusRepository;
//    @InjectMocks
//    private PaymentStatusServiceImpl paymentStatusServiceImpl;
//
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testGetAllPaymentStatuses_returnsCorrectStatuses() {
//        PaymentStatus paymentStatus1 = new PaymentStatus();
//        paymentStatus1.setId(1L);
//        paymentStatus1.setStatusName("Paid");
//
//        PaymentStatus paymentStatus2 = new PaymentStatus();
//        paymentStatus2.setId(2L);
//        paymentStatus2.setStatusName("Unpaid");
//
//        List<PaymentStatus> paymentStatuses = Arrays.asList(paymentStatus1, paymentStatus2);
//
//        when(paymentStatusRepository.findAll()).thenReturn(paymentStatuses);
//
//        List<PaymentStatusGetDto> result = paymentStatusServiceImpl.getAll();
//
//        assertEquals(2, result.size());
//        assertEquals(1L, result.get(0).id());
//        assertEquals("Paid", result.get(0).statusName());
//        assertEquals(2L, result.get(1).id());
//        assertEquals("Unpaid", result.get(1).statusName());
//        verify(paymentStatusRepository, times(1)).findAll();
//    }
//
//    @Test
//    void testGetByIdWithExistentId() throws PaymentStatusNotFoundException {
//        PaymentStatus paymentStatus = new PaymentStatus();
//        paymentStatus.setId(1L);
//        paymentStatus.setStatusName("Paid");
//
//        when(paymentStatusRepository.findById(1L)).thenReturn(Optional.of(paymentStatus));
//
//        PaymentStatusGetDto result = paymentStatusServiceImpl.getById(1L);
//
//        assertEquals(1L, result.id());
//        assertEquals("Paid", result.statusName());
//        verify(paymentStatusRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    void testGetByIdWithNonExistentId() {
//        when(paymentStatusRepository.findById(1L)).thenReturn(Optional.empty());
//
//        assertThrows(PaymentStatusNotFoundException.class, () -> paymentStatusServiceImpl.getById(1L));
//        verify(paymentStatusRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    void testGetByIdWithNullId() {
//        assertThrows(NullPointerException.class, () -> paymentStatusServiceImpl.getById(null));
//    }
//
//    @Test
//    void testGetByNameWithExistentName() throws PaymentStatusNotFoundException {
//        PaymentStatus paymentStatus = new PaymentStatus();
//        paymentStatus.setId(1L);
//        paymentStatus.setStatusName("Paid");
//
//        when(paymentStatusRepository.findByStatusName("Paid")).thenReturn(Optional.of(paymentStatus));
//
//        PaymentStatusGetDto result = paymentStatusServiceImpl.getByName("Paid");
//
//        assertEquals(1L, result.id());
//        assertEquals("Paid", result.statusName());
//        verify(paymentStatusRepository, times(1)).findByStatusName("Paid");
//    }
//
//    @Test
//    void testGetByNameWithNonExistentName() {
//        when(paymentStatusRepository.findByStatusName("Paid")).thenReturn(Optional.empty());
//
//        assertThrows(PaymentStatusNotFoundException.class, () -> paymentStatusServiceImpl.getByName("Paid"));
//        verify(paymentStatusRepository, times(1)).findByStatusName("Paid");
//    }
//
//    @Test
//    void testGetByNameWithNullName() {
//        assertThrows(NullPointerException.class, () -> paymentStatusServiceImpl.getByName(null));
//    }
//
//    @Test
//    void testCreatePaymentStatusWithValidStatusName() throws StatusNameAlreadyExistsException {
//        PaymentStatusCreateDto paymentStatusCreateDto = new PaymentStatusCreateDto("Paid");
//
//        PaymentStatus paymentStatus = new PaymentStatus();
//        paymentStatus.setId(1L);
//        paymentStatus.setStatusName("Paid");
//
//        when(paymentStatusRepository.save(any(PaymentStatus.class))).thenReturn(paymentStatus);
//
//        PaymentStatusGetDto result = paymentStatusServiceImpl.create(paymentStatusCreateDto);
//
//        assertEquals(1L, result.id());
//        assertEquals("Paid", result.statusName());
//        verify(paymentStatusRepository, times(1)).save(any(PaymentStatus.class));
//    }
//
//    @Test
//    void testCreatePaymentStatusWithNullStatusName() {
//        PaymentStatusCreateDto paymentStatusCreateDto = new PaymentStatusCreateDto(null);
//
//        assertThrows(NullPointerException.class, () -> paymentStatusServiceImpl.create(paymentStatusCreateDto));
//    }
//
//    @Test
//    void testCreatePaymentStatusWithEmptyStatusName() {
//        PaymentStatusCreateDto paymentStatusCreateDto = new PaymentStatusCreateDto("");
//
//        assertThrows(IllegalArgumentException.class, () -> paymentStatusServiceImpl.create(paymentStatusCreateDto));
//    }
//
//    @Test
//    void testCreatePaymentStatusWithBlankStatusName() {
//        PaymentStatusCreateDto paymentStatusCreateDto = new PaymentStatusCreateDto(" ");
//
//        assertThrows(IllegalArgumentException.class, () -> paymentStatusServiceImpl.create(paymentStatusCreateDto));
//    }
//
//    @Test
//    void testCreatePaymentStatusWithStatusNameContainingLowercaseLetters() {
//        PaymentStatusCreateDto paymentStatusCreateDto = new PaymentStatusCreateDto("Paid");
//
//        assertThrows(IllegalArgumentException.class, () -> paymentStatusServiceImpl.create(paymentStatusCreateDto));
//    }
//
//    @Test
//    void testCreatePaymentStatusWithStatusNameContainingNumbers() {
//        PaymentStatusCreateDto paymentStatusCreateDto = new PaymentStatusCreateDto("PAID1");
//
//        assertThrows(IllegalArgumentException.class, () -> paymentStatusServiceImpl.create(paymentStatusCreateDto));
//    }
//
//    @Test
//    void testUpdatePaymentStatus() throws StatusNameAlreadyExistsException, PaymentStatusNotFoundException {
//        PaymentStatus paymentStatus = new PaymentStatus();
//        paymentStatus.setId(1L);
//        paymentStatus.setStatusName("Paid");
//
//        PaymentStatusCreateDto paymentStatusCreateDto = new PaymentStatusCreateDto("Unpaid");
//
//        when(paymentStatusRepository.findById(1L)).thenReturn(Optional.of(paymentStatus));
//        when(paymentStatusRepository.save(any(PaymentStatus.class))).thenReturn(paymentStatus);
//
//        PaymentStatusGetDto result = paymentStatusServiceImpl.update(1L, paymentStatusCreateDto);
//
//        assertEquals("Unpaid", result.statusName());
//        verify(paymentStatusRepository, times(1)).findById(1L);
//        verify(paymentStatusRepository, times(1)).save(any(PaymentStatus.class));
//    }
//
//
//    @Test
//    void testUpdatePaymentStatusWithNonExistentId() {
//        PaymentStatusCreateDto paymentStatusCreateDto = new PaymentStatusCreateDto("Unpaid");
//
//        when(paymentStatusRepository.findById(1L)).thenReturn(Optional.empty());
//
//        assertThrows(PaymentStatusNotFoundException.class, () -> paymentStatusServiceImpl.update(1L, paymentStatusCreateDto));
//        verify(paymentStatusRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    void testUpdatePaymentStatusWithNullStatusName() {
//        PaymentStatus paymentStatus = new PaymentStatus();
//        paymentStatus.setId(1L);
//        paymentStatus.setStatusName("Paid");
//
//        PaymentStatusCreateDto paymentStatusCreateDto = new PaymentStatusCreateDto(null);
//
//        when(paymentStatusRepository.findById(1L)).thenReturn(Optional.of(paymentStatus));
//
//        assertThrows(NullPointerException.class, () -> paymentStatusServiceImpl.update(1L, paymentStatusCreateDto));
//        verify(paymentStatusRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    void testUpdatePaymentStatusWithEmptyStatusName() {
//        PaymentStatus paymentStatus = new PaymentStatus();
//        paymentStatus.setId(1L);
//        paymentStatus.setStatusName("Paid");
//
//        PaymentStatusCreateDto paymentStatusCreateDto = new PaymentStatusCreateDto("");
//
//        when(paymentStatusRepository.findById(1L)).thenReturn(Optional.of(paymentStatus));
//
//        assertThrows(IllegalArgumentException.class, () -> paymentStatusServiceImpl.update(1L, paymentStatusCreateDto));
//        verify(paymentStatusRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    void testUpdatePaymentStatusWithBlankStatusName() {
//        PaymentStatus paymentStatus = new PaymentStatus();
//        paymentStatus.setId(1L);
//        paymentStatus.setStatusName("Paid");
//
//        PaymentStatusCreateDto paymentStatusCreateDto = new PaymentStatusCreateDto(" ");
//
//        when(paymentStatusRepository.findById(1L)).thenReturn(Optional.of(paymentStatus));
//
//        assertThrows(IllegalArgumentException.class, () -> paymentStatusServiceImpl.update(1L, paymentStatusCreateDto));
//        verify(paymentStatusRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    void testUpdatePaymentStatusWithStatusNameContainingLowercaseLetters() {
//        PaymentStatus paymentStatus = new PaymentStatus();
//        paymentStatus.setId(1L);
//        paymentStatus.setStatusName("Paid");
//
//        PaymentStatusCreateDto paymentStatusCreateDto = new PaymentStatusCreateDto("Paid");
//
//        when(paymentStatusRepository.findById(1L)).thenReturn(Optional.of(paymentStatus));
//
//        assertThrows(IllegalArgumentException.class, () -> paymentStatusServiceImpl.update(1L, paymentStatusCreateDto));
//        verify(paymentStatusRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    void testUpdatePaymentStatusWithStatusNameContainingNumbers() {
//        PaymentStatus paymentStatus = new PaymentStatus();
//        paymentStatus.setId(1L);
//        paymentStatus.setStatusName("Paid");
//
//        PaymentStatusCreateDto paymentStatusCreateDto = new PaymentStatusCreateDto("PAID1");
//
//        when(paymentStatusRepository.findById(1L)).thenReturn(Optional.of(paymentStatus));
//
//        assertThrows(IllegalArgumentException.class, () -> paymentStatusServiceImpl.update(1L, paymentStatusCreateDto));
//        verify(paymentStatusRepository, times(1)).findById(1L);
//    }
//
//
//    @Test
//    void testDeletePaymentStatusWithExistentId() throws PaymentStatusInUseException, PaymentStatusNotFoundException {
//        PaymentStatus paymentStatus = new PaymentStatus();
//        paymentStatus.setId(1L);
//        paymentStatus.setStatusName("Paid");
//
//        when(paymentStatusRepository.findById(1L)).thenReturn(Optional.of(paymentStatus));
//
//        paymentStatusServiceImpl.delete(1L);
//
//        verify(paymentStatusRepository, times(1)).findById(1L);
//        verify(paymentStatusRepository, times(1)).deleteById(1L);
//    }
//
//    @Test
//    void testDeletePaymentStatusWithNonExistentId() {
//        when(paymentStatusRepository.findById(1L)).thenReturn(Optional.empty());
//
//        assertThrows(PaymentStatusNotFoundException.class, () -> paymentStatusServiceImpl.delete(1L));
//        verify(paymentStatusRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    void testDeletePaymentStatusWithNullId() {
//        assertThrows(NullPointerException.class, () -> paymentStatusServiceImpl.delete(null));
//    }
//
//    @Test
//    void testDeletePaymentStatusWithNegativeId() {
//        assertThrows(IllegalArgumentException.class, () -> paymentStatusServiceImpl.delete(-1L));
//    }
//
//    @Test
//    void testDeletePaymentStatusWithZeroId() {
//        assertThrows(IllegalArgumentException.class, () -> paymentStatusServiceImpl.delete(0L));
//    }
//
//    @Test
//    void testDeletePaymentStatusWithIdNotInDatabase() {
//        when(paymentStatusRepository.existsById(1L)).thenReturn(false);
//
//        assertThrows(PaymentStatusNotFoundException.class, () -> paymentStatusServiceImpl.delete(1L));
//        verify(paymentStatusRepository, times(1)).existsById(1L);
//    }


}