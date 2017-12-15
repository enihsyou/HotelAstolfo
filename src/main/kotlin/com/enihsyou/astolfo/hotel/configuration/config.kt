package com.enihsyou.astolfo.hotel.configuration


//@Configuration
//@EnableWebSecurity
//class WebSecurityConfig : WebSecurityConfigurerAdapter() {
//  @Throws(Exception::class)
//  override fun configure(http: HttpSecurity) {
//    http
//        .authorizeRequests()
//
//        .antMatchers("/api").authenticated()
//        .anyRequest().permitAll()
//        .and()
//        .formLogin()
//        .loginPage("/login").permitAll()
//        .and()
//        .logout().permitAll()
//  }
//
//  override fun configure(auth: AuthenticationManagerBuilder) {
//    auth.jdbcAuthentication()
//        .authoritiesByUsernameQuery("")
//  }
//
//  @Autowired
//  @Throws(Exception::class)
//  fun configureGlobal(auth: AuthenticationManagerBuilder) {
//    auth
//        .inMemoryAuthentication()
//        .withUser("user").password("password").roles("USER")
//
//  }
//}
