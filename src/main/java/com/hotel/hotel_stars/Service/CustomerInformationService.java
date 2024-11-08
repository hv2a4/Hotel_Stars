package com.hotel.hotel_stars.Service;

import com.hotel.hotel_stars.DTO.CustomerInformationDto;
import com.hotel.hotel_stars.Entity.CustomerInformation;
import com.hotel.hotel_stars.Models.customerInformationModel;
import com.hotel.hotel_stars.Repository.CustomerInformationRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CustomerInformationService {
    @Autowired
    private CustomerInformationRepository customerInformationRepository;

    public CustomerInformationDto convertToDto(CustomerInformation customerInformation) {
        return new CustomerInformationDto(
                customerInformation.getId(),
                customerInformation.getCccd(),
                customerInformation.getFullname(),
                customerInformation.getPhone(),
                customerInformation.getGender(),
                customerInformation.getBirthday(),
                customerInformation.getImgFirstCard(),
                customerInformation.getImgLastCard()
        );
    }

    public List<CustomerInformationDto> getAllCustomerInformation() {
        List<CustomerInformation> customerInformationList = customerInformationRepository.findAll();
        return customerInformationList.stream()
                .map(this::convertToDto)
                .toList();
    }

    // Thêm khách hàng mới
    public CustomerInformationDto addCustomerInformation(customerInformationModel customerInformationModel) {
        try {
            CustomerInformation customerInformation = new CustomerInformation();
            customerInformation.setCccd(customerInformationModel.getCccd());
            customerInformation.setFullname(customerInformationModel.getFullname());
            customerInformation.setPhone(customerInformationModel.getPhone());
            customerInformation.setGender(customerInformationModel.getGender());
            customerInformation.setBirthday(customerInformationModel.getBirthday());
            customerInformation.setImgFirstCard(customerInformationModel.getImgFirstCard());
            customerInformation.setImgLastCard(customerInformationModel.getImgLastCard());

            // Lưu vào cơ sở dữ liệu
            CustomerInformation savedCustomerInformation = customerInformationRepository.save(customerInformation);
            return convertToDto(savedCustomerInformation);

        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Có lỗi xảy ra do vi phạm tính toàn vẹn dữ liệu", e);
        } catch (Exception e) {
            throw new RuntimeException("Có lỗi xảy ra khi thêm vào!", e);
        }
    }

    // Cập nhật khách hàng
    public CustomerInformationDto updateCustomerInformation(Integer customerId, customerInformationModel customerInformationModel) {
        List<String> errorMessages = new ArrayList<>(); // Danh sách lưu trữ các thông báo lỗi

        // Kiểm tra xem khách hàng có tồn tại không
        Optional<CustomerInformation> existingCustomerOpt = customerInformationRepository.findById(customerId);

        // Kiểm tra nếu không tìm thấy khách hàng
        if (existingCustomerOpt.isEmpty()) {
            errorMessages.add("Khách hàng không tồn tại");
        }

        // Nếu có lỗi, ném ngoại lệ với thông báo lỗi
        if (!errorMessages.isEmpty()) {
            throw new ValidationException(String.join(", ", errorMessages));
        }

        // Lấy khách hàng đã tồn tại
        CustomerInformation existingCustomer = existingCustomerOpt.get();

        try {
            // Cập nhật thông tin khách hàng
            existingCustomer.setPhone(customerInformationModel.getPhone());
            existingCustomer.setFullname(customerInformationModel.getFullname());
            existingCustomer.setCccd(customerInformationModel.getCccd());
            existingCustomer.setGender(customerInformationModel.getGender());
            existingCustomer.setBirthday(customerInformationModel.getBirthday());
            existingCustomer.setImgFirstCard(customerInformationModel.getImgFirstCard());
            existingCustomer.setImgLastCard(customerInformationModel.getImgLastCard());

            // Lưu thay đổi vào cơ sở dữ liệu
            CustomerInformation updatedCustomer = customerInformationRepository.save(existingCustomer);

            // Chuyển đổi sang DTO và trả về
            return convertToDto(updatedCustomer);

        } catch (DataIntegrityViolationException e) {
            // Xử lý lỗi vi phạm tính toàn vẹn dữ liệu (VD: trùng lặp CCCD hoặc số điện thoại)
            throw new RuntimeException("Có lỗi xảy ra do vi phạm tính toàn vẹn dữ liệu", e);
        } catch (Exception e) {
            // Xử lý lỗi chung
            throw new RuntimeException("Có lỗi xảy ra khi cập nhật thông tin khách hàng", e);
        }
    }

    // Xóa khách hàng
    public void deleteCustomerInformation(Integer id) {
        Optional<CustomerInformation> customerInformationOptional = customerInformationRepository.findById(id);
        if (customerInformationOptional.isEmpty()) {
            throw new NoSuchElementException("Khách hàng không tồn tại");
        }
        customerInformationRepository.deleteById(id);
    }

}