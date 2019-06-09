package org.springboot.example.example.webmvc.crud.endpoint;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springboot.example.example.webmvc.crud.entity.Department;
import org.springboot.example.example.webmvc.crud.entity.Employee;
import org.springboot.example.example.webmvc.crud.service.DepartmentService;
import org.springboot.example.example.webmvc.crud.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * @author Calvin
 * @titile: 控制器
 * @date 2019/3/8
 * @since 1.0
 */
@Api(value = "Web MVC", tags = {"SpringBoot Web MVC API"})
@Controller
public class EmployeeEndpoint {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private DepartmentService departmentService;

    @ApiOperation(value = "查询-所有员工")
    @GetMapping("/emps")
    public String list(Model model) {
        Collection<Employee> employees = employeeService.queryAll();
        //放在请求域中
        model.addAttribute("emps", employees);
        // thymeleaf默认就会拼串
        // classpath:/templates/xxxx.html
        return "emp/list";
    }

    @ApiOperation(value = "员工添加页面")
    @GetMapping("/emp")
    public String toAddPage(Model model) {
        //来到添加页面,查出所有的部门，在页面显示
        Collection<Department> departments = departmentService.queryAll();
        model.addAttribute("depts", departments);
        return "emp/add";
    }

    //SpringMVC自动将请求参数和入参对象的属性进行一一绑定；要求请求参数的名字和javaBean入参的对象里面的属性名是一样的
    @ApiOperation(value = "添加-员工")
    @PostMapping("/emp")
    public String addEmp(Employee employee) {
        //来到员工列表页面
        System.out.println("保存的员工信息：" + employee);
        //保存员工
        employeeService.create(employee);
        // redirect: 表示重定向到一个地址  /代表当前项目路径
        // forward: 表示转发到一个地址
        return "redirect:/emps";
    }

    @ApiOperation(value = "查看-员工详情")
    @GetMapping("/emp/{id}")
    public String toEditPage(@PathVariable("id") Long id, Model model) {
        Employee employee = employeeService.queryByPrimary(id);
        model.addAttribute("emp", employee);

        //页面要显示所有的部门列表
        Collection<Department> departments = departmentService.queryAll();
        model.addAttribute("depts", departments);
        //回到修改页面(add是一个修改添加二合一的页面);
        return "emp/add";
    }

    @ApiOperation(value = "更新-员工信息")
    @PutMapping("/emp")
    public String updateEmployee(Employee employee) {
        System.out.println("修改的员工数据：" + employee);
        employeeService.update(employee);
        return "redirect:/emps";
    }

    //员工删除
    @ApiOperation(value = "删除-员工")
    @DeleteMapping("/emp/{id}")
    public String deleteEmployee(@PathVariable("id") Long id) {
        employeeService.delete(Employee.builder().id(id).build());
        return "redirect:/emps";
    }


}


//    @ApiOperation(value = "detail", notes = "detail", httpMethod = "POST")
//    @RequestMapping("detail")
//    public ModelAndView detail(Long id){
//        // 访问模型层得到的数据
//        User user = userService.queryByPrimary(id);
//        // 模型和视图
//        ModelAndView mv = new ModelAndView();
//        // 定义模型视图, 在配置定制了视图解析器，就会 /WEB-INF/jsp/user/detail.jsp
//        mv.setViewName("user/detail");
//        // 加入数据模型
//        mv.addObject("user", user);
//        // 返回模型和视图
//        return mv;
//    }
//
//    @ApiOperation(value = "detail", notes = "detail", httpMethod = "POST")
//    @RequestMapping("detail/json")
//    public ModelAndView detailForJson(Long id){
//        // 访问模型层得到的数据
//        User user = userService.queryByPrimary(id);
//        // 模型和视图
//        ModelAndView mv = new ModelAndView();
//        // 生成Json视图
//        MappingJackson2JsonView mappingJackson2JsonView = new MappingJackson2JsonView();
//        mv.setView(mappingJackson2JsonView);
//        // 加入数据模型
//        mv.addObject("user", user);
//        // 返回模型和视图
//        return mv;
//    }



