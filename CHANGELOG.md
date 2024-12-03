# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Calendar Versioning](https://calver.org/) of
the following form: YYYY.0M.0D.

## 2024.12.03

### Added

- Designed kernel and enhanced interfaces for ProjectileCalculator component
- Added `calculateMaxRange` and `isAtMaxRange` methods.
- Added accessor methods in the kernel.

### Updated

- Changed design to include more accessor methods in the kernel (for mass, speed, direction).
- Changed kernel to contain global constants such as G=9.81.
- Methods and fields regarding projectile mass have been removed, since they are irrelevant for kinematic equations.

## 2024.11.29

### Added

- Designed a proof of concept for ProjectileCalculator component

### Updated

- Changed design to include accessor methods in the kernel.

## 2024.10.17

### Added

- Designed a Frequency Tracker component
- Designed a Assignment Tracker component
- Designed a Trajectory Calculator component
