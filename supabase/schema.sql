-- DS KUSHWAHA GROUP app: Supabase starter schema
-- Run in Supabase SQL Editor. Do not put a service-role key in the Android app.
create extension if not exists pgcrypto;

create table if not exists public.profiles (
  id uuid primary key references auth.users(id) on delete cascade,
  full_name text not null default '',
  phone text unique,
  role text not null default 'customer' check (role in ('customer','admin')),
  created_at timestamptz not null default now()
);

create table if not exists public.businesses (
  id text primary key,
  name text not null,
  name_hi text not null default '',
  description text not null default '',
  active boolean not null default true,
  sort_order int not null default 0
);

create table if not exists public.products (
  id uuid primary key default gen_random_uuid(),
  business_id text not null references public.businesses(id),
  name text not null,
  description text not null default '',
  price numeric(12,2),
  stock int not null default 0 check (stock >= 0),
  image_url text,
  active boolean not null default true,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

create table if not exists public.coupons (
  id uuid primary key default gen_random_uuid(),
  code text not null unique,
  description text not null default '',
  discount_type text not null check (discount_type in ('percent','fixed')),
  discount_value numeric(12,2) not null check (discount_value > 0),
  minimum_order numeric(12,2) not null default 0,
  starts_at timestamptz,
  expires_at timestamptz,
  active boolean not null default true,
  created_at timestamptz not null default now()
);

create table if not exists public.orders (
  id uuid primary key default gen_random_uuid(),
  customer_id uuid not null references auth.users(id),
  customer_name text not null,
  phone text not null,
  delivery_address text not null default '',
  items jsonb not null,
  subtotal numeric(12,2),
  coupon_code text,
  status text not null default 'new'
    check (status in ('new','confirmed','processing','shipped','completed','cancelled')),
  created_at timestamptz not null default now()
);

create or replace function public.is_admin()
returns boolean language sql stable security definer
set search_path = public
as $$
  select exists (
    select 1 from public.profiles
    where id = auth.uid() and role = 'admin'
  );
$$;

alter table public.profiles enable row level security;
alter table public.businesses enable row level security;
alter table public.products enable row level security;
alter table public.coupons enable row level security;
alter table public.orders enable row level security;

drop policy if exists "Users read own profile or admins read all" on public.profiles;
create policy "Users read own profile or admins read all" on public.profiles
for select to authenticated using (id = auth.uid() or public.is_admin());
drop policy if exists "Users update own basic profile" on public.profiles;
create policy "Users update own basic profile" on public.profiles
for update to authenticated using (id = auth.uid()) with check (id = auth.uid() and role = 'customer');
drop policy if exists "Admins manage profiles" on public.profiles;
create policy "Admins manage profiles" on public.profiles
for all to authenticated using (public.is_admin()) with check (public.is_admin());

drop policy if exists "Anyone reads active businesses" on public.businesses;
create policy "Anyone reads active businesses" on public.businesses
for select to anon, authenticated using (active or public.is_admin());
drop policy if exists "Admins manage businesses" on public.businesses;
create policy "Admins manage businesses" on public.businesses
for all to authenticated using (public.is_admin()) with check (public.is_admin());

drop policy if exists "Anyone reads active products" on public.products;
create policy "Anyone reads active products" on public.products
for select to anon, authenticated using (active or public.is_admin());
drop policy if exists "Admins manage products" on public.products;
create policy "Admins manage products" on public.products
for all to authenticated using (public.is_admin()) with check (public.is_admin());

drop policy if exists "Anyone reads valid active coupons" on public.coupons;
create policy "Anyone reads valid active coupons" on public.coupons
for select to anon, authenticated using (
  active and (starts_at is null or starts_at <= now())
  and (expires_at is null or expires_at >= now())
  or public.is_admin()
);
drop policy if exists "Admins manage coupons" on public.coupons;
create policy "Admins manage coupons" on public.coupons
for all to authenticated using (public.is_admin()) with check (public.is_admin());

drop policy if exists "Customers read own orders; admins read all" on public.orders;
create policy "Customers read own orders; admins read all" on public.orders
for select to authenticated using (customer_id = auth.uid() or public.is_admin());
drop policy if exists "Customers create own orders" on public.orders;
create policy "Customers create own orders" on public.orders
for insert to authenticated with check (customer_id = auth.uid());
drop policy if exists "Admins update orders" on public.orders;
create policy "Admins update orders" on public.orders
for update to authenticated using (public.is_admin()) with check (public.is_admin());

-- Seed the five approved business units. Safe to re-run.
insert into public.businesses (id, name, name_hi, description, sort_order) values
('properties','DS PROPERTIES','डीएस प्रॉपर्टीज','Plots, land, houses and apartments',1),
('building-materials','DS BUILDING MATERIALS','डीएस बिल्डिंग मटेरियल्स','Construction and building materials',2),
('solar','DS SOLAR TECHNOLOGY','डीएस सोलर टेक्नोलॉजी','Solar products and installation enquiries',3),
('gadgets','DS SMART GADGETS','डीएस स्मार्ट गैजेट्स','Electronics and smart gadgets',4),
('lifestyle','DS LIFESTYLE BRANDS','डीएस लाइफस्टाइल ब्रांड्स','Apparels, footwear and accessories',5)
on conflict (id) do update set name=excluded.name, name_hi=excluded.name_hi,
description=excluded.description, sort_order=excluded.sort_order;

-- After creating your own account, promote ONLY your account from the SQL Editor:
-- update public.profiles set role = 'admin' where phone = '+91XXXXXXXXXX';
-- The app must never let customers set their own role.
